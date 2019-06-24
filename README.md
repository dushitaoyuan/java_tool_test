# java_tool_test
java工具类测试
## dozer-example
dozer bean工具例子及扩展 

## 测试结果

|lib|Benchmark avg score|
|-|-|
|dozer|11.706|
|org.apache.commons.beanutils.BeanUtils|8.646|
|org.apache.commons.beanutils.PropertyUtils|8.830|

|org.springframework.beans.BeanUtils|1.890|

可以查看 org.springframework.beans.BeanUtils 最快,  org.apache.commons.beanutils.BeanUtils其次


但是两者可扩展性较低,不过一般的系统对性能要求也没有那么严格,也没有必要压榨beancopy消耗的
那点性能,所以根据自己的需要选择一个可扩展性较高的工具即可
