# travelApp

==== 旅游app的安卓端
后台： https://github.com/540871129/AppBackend

------------

#### 说明：

- 此旅游app项目是本人的毕业设计，包括了后台和安卓端，之前没接触过安卓，然后为了毕业设计，边学边码，时不时的问了周围搞安卓的同学，终于吧这个可恶的安卓端做好了。

- 总体来说样子还过得去，不足的地方还是很多的，像Fragment和activity各种乱用了，还有些bug自己也懒修了，特别是在Tabhost的第二个tab，因为嵌套了viewpage，viewpage里面还加了tablayout导致第一次显示居然没有标题了，切换其他tab后回来才显示，没办法时间赶加上自己安卓水到不行，还各种极度厌恶java,看到那代码简直就没激情了。但是觉得还是有些参考价值的，就贡献出来看看。

- app界面：懒得传图了，就给个isux的传送门，有兴趣的朋友去看就好了。

-- by Daath

------------

##### 第三方sdk:

- 融云即时通讯
- 百度云推送，定位

##### 框架：

- asyncHttpClient：异步网络请求库
- GreenDao: sqlit3的ORM
- PullToRefresh:上拉加载下拉刷新
