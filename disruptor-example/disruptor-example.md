## disruptor-example 简介

disruptor 简单使用例子,参见com.taoyuanx.disruptor.example.LogEventMain



## 使用场景简介

## 消息发送
场景描述:<br/>
有一耗时业务逻辑,处理完毕后,还需向多个第三方等推送通知,此场景可借助disruptor 事件机制


## 业务日志存储
场景描述:<br/>
特点:业务简单,保存频繁,非核心业务逻辑
可借助,disruptor 事件机制,批量转存


## 登录计数,最后登录时间,ip等

登录事件触发登录处理,异步处理

## 邮件,短信,等耗时操作

