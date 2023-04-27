# 毕业方向辅导小程序-项目亮点



## Spring Security

本项目了整合了Spring Security来做安全框架，是Spring家族中的一个非常丰富与完善的安全框架

一般Web应用的需要进行**认证**和**授权**。

​		**认证：验证当前访问系统的是不是本系统的用户，并且要确认具体是哪个用户**

​		**授权：经过认证后判断当前用户是否有权限进行某个操作**

认证和授权也是SpringSecurity作为安全框架的核心功能。

本项目有三个种不同的角色：分别是学生、老师、管理员

每个用户都有1对1的角色，因此用户和角色关系采用了1对1的关系

而一个角色可能有多个权限，这里采用了多对多对关系，这是个**RBAC权限模型**的简单应用



## 二维码生成与识别

这里采用了一个现成的轮子-`Hutool`的二维码工具类（`QrCodeUtil`）对二维码进行简单的生成和识别，其依赖于谷歌的二维码识别工具-`zxing`工具包

二维码的编码值则是一个随机生成的uuid，该uuid对应了一个唯一的用户id

### 解决二维码超时问题

不难发现这里二维码如果不设置失效时间的话可能会引发一系列安全问题，这里引用了中间件-`redis`作为缓存

我们将**键设置为uuid的值**，则该uuid对应的**值为用户的id**，这里我们只需要设置个过期时间则可以完美的解决二维码时效的问题



## 商城的缓存问题

这里我采用`redis`来解决商品缓存问题，以对付访问量较大的情况

### 缓存时效性问题

如果商品数据库的值改了，而缓存没有及时更新，这会导致缓存时效性不一致

这里采用主动更新，就是更新数据库的时候，先更新数据库，再删除缓存，这样就解决了缓存与数据库的双写一致性的问题

### 缓存穿透问题

如果用户查询的商品id在数据库中也不存在，这种情况无论如何都会查询数据库，从而给数据库造成了比较大的压力

这里我们缓存了空值，牺牲内存的代价来增加系统的可用性

### 缓存击穿问题

如果一个高访问量的商品的缓存因为缓存过期或主动更新了，此时会有很多个请求来执行查询数据库的操作，会给数据库很大的压力

这里有两种解决方法，逻辑时间字段和互斥锁解决

这里我用了较为简单的互斥锁解决缓存击穿问题：

实现缓存重建的时候只能有一个线程进去查询数据库与添加缓存。

这里用redis的`setnx`关键字来实现锁的功能-解决在分布式系统下会出现的问题



## 用户下单场景-乐观锁解决超卖问题

这里引用下代码

```java
// 并没有解决并发安全问题！（已解决，详见73行，原来用的userScore并不是最新值）
//前提：没有一人一单的需求
//这里使用乐观锁解决超卖问题（简单的秒杀应用）
User user = UserUtils.getUser().getUser();
Goods goods = Optional.ofNullable(goodsService.getGoods(goodsId))
        .orElseThrow(() -> new BizException(ResultCodeEnum.GOODS_NOT_EXIST));

//1. 判断用户当前的积分是否能够购买商品
//1.1 获取当前用户的积分
Long userScore = userService.getById(user.getId()).getScore();
//1.2 与商品所需的积分进行比较
if (goods.getScore() > userScore) {
    throw new BizException(ResultCodeEnum.INSUFFICIENT_SCORE);
}
//2. 走到这里表示用户积分足够，开始判断库存是否足够
if (goods.getStock() <= 0) {
    //2.1 库存小于等于0，表示以及卖完
    throw new BizException(ResultCodeEnum.INSUFFICIENT_INVENTORY);
}
//3. 开始扣减用户积分和库存
//3.1 扣除库存
boolean success = goodsService.lambdaUpdate()
        .setSql("stock = stock - 1")
        .eq(Goods::getId, goodsId)
        //更新的同时需检测库存是否大于0
        .gt(Goods::getStock, 0)
        .update();
//3.2 检测扣除库存是否成功
if (!success) {
    //3.3 失败情况
    throw new BizException(ResultCodeEnum.INSUFFICIENT_INVENTORY);
}
//3.4 开始扣除积分
success = userService.lambdaUpdate()
        .setSql("score = score - " + goods.getScore())
        //.set(User::getScore, userScore - goods.getScore())
        .eq(User::getId, user.getId())
        //同时检测当前积分是否大于等于商品积分
        .ge(User::getScore, goods.getScore())
        .update();
//3.3 检测扣除积分是否成功
if (!success) {
    throw new BizException(ResultCodeEnum.INSUFFICIENT_SCORE);
}

//4. 走到这里表示用户以及购买到商品了！接下来生成订单
Order order = new Order();
order.setUserId(user.getId());
order.setGoodsId(goodsId);
order.setStatusEnum(OrderStatusEnum.NOT_USE);
save(order);

//这里删除redis缓存的商品信息和用户个人信息
redisCache.deleteObject(USER_CACHE_KEY + user.getId());
redisCache.deleteObject(GOODS_CACHE_KEY + goodsId);

//记录积分流水
scoreRecordsService.insertRecord(userScore, -goods.getScore(), order.getId(), ScoreRecordTypesEnum.BUY_GOODS);
```

简单介绍下，这里主要是并发访问的时候查询和更新数据库操作不同步导致的，简单粗暴的方式就是直接加一个**悲观锁**限制一个线程，但是会导致系统变慢

这里用乐观锁就是比较好的解决方法，具体是在执行数据库更新操作的时候同时**再次检查当前的积分或库存是否足够**，如果不足够直接**抛出异常**，随后就是**事务回滚操作**，成功的话则生成订单，**并主动更新redis缓存**



## BitMap实现记录用户打卡信息保存

这里采用`Redis`的`BitMap`数据结构来保存每个用户的每月打卡情况

![截屏2023-04-03 18.21.04](https://typora-1312272916.cos.ap-shanghai.myqcloud.com//img%E6%88%AA%E5%B1%8F2023-04-03%2018.21.04.png)

比如说今天4月3日用户打卡了一次，则根据当天在该月的第多少天的位置设置成1，表示用户在该天打卡了一次

```java
//1. 获取当前登录情况
Long userId = UserUtils.getUser().getUser().getId();
//2. 获取日期
LocalDateTime now = TimeUtils.getCurrentTime();
//3. 拼接key
String keySuffix = now.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
String key = USER_CLOCK_KEY + userId + keySuffix;
//4. 获取今天是本月的第几天
int dayOfMonth = now.getDayOfMonth();
//5. 写入redis setbit key offset 1
stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
```







## 消息队列实现微信的消息订阅功能-待定





