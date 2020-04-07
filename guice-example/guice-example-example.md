## guice-example 简介

guice 简单使用例子,参见com.taoyuanx.guice.example



## 使用场景简介

### guice常用注解简介
* @ImplementedBy
```java
# 注解在service上,指定service的实现
例子:
@ImplementedBy(TruckCarServiceImpl.class)
public interface CarService {
    void dirve();
}
```
*  @Inject  @Named

```java
#Inject 注解在方法,构造方法,属性上 和@Autowired 用法类似
#@Named 同  @Qualifier   
#例子:
    @Inject
    @Named("bus")
    CarService carService;
```

* @Singleton 
声明作用域,参见:Scopes:SINGLETON|NO_SCOPE,也可以自定义

*  @Provides 声明提供类
```java


    @Provides
    public CarService carService(@Named("truck") CarService truck, @Named("bus") CarService bus) {
       if(  Math.random()>0.5){
           return truck;
       }
       return  bus;

    }
``` 

### 高级用法
利用guice实现 类似spring的扫描,参见:com.taoyuanx.guice.example.test.module.BasePackageModule
目前支持注解:Service,Component,Primary
#### 核心逻辑
* 包扫描
* guice注入
1. 有实现 注入所有实现
2. 没有实现注入自身
3. 为支持 @Name 类似@Qualifier ,需保证 直接 使用 @Inject 可以找到primary依赖,如无primary依赖,注入实现列表的第一个
