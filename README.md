# Bos_study

# 快递员列表（无条件）
  * 前端
      1. 修改DataGrid的URL属性，向action发送请求数据
  * 后端
      1. 采用属性驱动接收 page,rows  
          1. PageRequest(int page, int rows)-->Pageable
          2. 调用业务层 得到Page<Courier> 对象分页集合数据
      2. 调用Dao的findAll传入Pageable对象 拿到page对象
      3. 页面接收数据类型为｛total：x,rows:{}｝键值对的形式
             将page对象的totalElements和content封装到Map集合中去（推到栈顶），让struts2序列化返回给客户端（datagrid）
# 快递员列表（有条件）
  * 前端
    1. 添加查询按钮并添加 window('open')方法，打开查询窗口
    2. 为窗口中的查询按钮添加点击事件
      1. 利用jquery serialize(),serializearry()将页面form中数据转换成｛key:value,key:value｝格式
      2. 基于自定义的方法将form参数转换成json绑定到datagrid（load方法）上（自动刷新）
  * 后端
    1. Action接收page,rows和四个查询参数（模型驱动封装到对象中）
      * 利用Specification 的匿名内部类封装查询条件
    2. Dao继承JpaSpecificationExcutor
    3. 实现一个Specification对象封装条件
      * 当传入了这个参数才需要拼装条件
    4. 将pageable specification传入findAll方法进行带条件的分页查询
    5. 拿到Page对象序列化
