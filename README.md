# README

参考博客：https://juejin.cn/post/6844903618928181261

### 一、理解 REST

**REST（Representational State Transfer）**,中文翻译叫“表述性状态转移”。是 Roy Thomas Fielding 在他2000年的博士论文中提出的。它与传统的 SOAP Web 服务区别在于，REST关注的是要处理的数据，而 SOAP 主要关注行为和处理。要理解好 REST，根据其首字母拆分出的英文更容易理解。

**表述性（Representational）**:对于 REST 来说，我们网络上的一个个URI资源可以用各种形式来表述，例如：XML、JSON或者HTML等。

**状态（State）：** REST 更关注资源的状态而不是对资源采取的行为。

**转移（Transfer）**：在网络传输过程中，REST 使资源以某种表述性形式从一个应用转移到另一个应用（如从服务端转移到客户端）。

```
具体来说，REST 中存在行为，它的行为是通过 HTTP 表示操作的方法来定义的即：GET、POST、PUT、DELETE、PATCH；GET用来获取资源，POST用来新建资源（也可以用于更新资源），PUT用来更新资源，DELETE用来删除资源，PATCH用来更新资源。 基于 REST 这样的观点，我们需要避免使用 REST服务、REST Web服务 这样的称呼，这些称呼多少都带有一些强调行为的味道。
```

### 二、使用 RESTful 架构设计使用误区

**RESTful 架构：**是基于 REST 思想的时下比较流行的一种互联网软件架构。它结构清晰、符合标准、易于理解、扩展方便，所以正得到越来越多网站的采用。

在没有足够了解 REST 的时候，我们很容易错误的将其视为 “基于 URL 的 Web 服务”，即将 REST 和 SOAP 一样，是一种远程过程调用（remote procedure call，RPC）的机制。但是 REST 和 RPC 几乎没有任何关系，RPC 是面向服务的，而 REST 是面向资源的，强调描述应用程序的事物和名词。这样很容易导致的一个结果是我们在设计 RESTful API 时，在 URI 中使用动词。例如：`GET /user/getUser/123`。正确写法应该是 `GET /user/123`。

### 三、 springMVC 支持 RESTful

在 spring 3.0 以后，spring 这对 springMVC 的一些增强功能对 RESTful 提供了良好的支持。在4.0后的版本中，spring 支持一下方式创建 REST 资源：

1. 控制器可以处理所有的 HTTP 方法，包含几个主要的 REST 方法：`GET、POST、PUT、DELETE、PATCH`；
2. 借助 spring 的视图解析器，资源能够以多种方式进行表述，包括将模型数据渲染为 `XML、JSON、Atom、已经 RSS 的 View` 实现；
3. 可以使用 `ContentNegotiatingViewResolver` 来选择最适合客户端的表述；
4. 借助 `@ResponseBody` 注解和各种 `HttpMethodConverter` 实现，能够替换基于视图的渲染方式；
5. 类似地，`@RequestBody` 注解以及 `HttpMethodConverter` 实现可以将传入的 HTTP 数据转化为传入控制器处理方法的 Java 对象；
6. 借助 `RestTemplate` ，spring 应用能够方便地使用 REST 资源。

### 四、基于Rest的Controller（控制器）

我们的 REST API :

- GET 方式请求 /api/user/ 返回用户列表
- GET 方式请求 /api/user/1返回id为1的用户
- POST 方式请求 /api/user/ 通过user对象的JSON 参数创建新的user对象
- PUT 方式请求 /api/user/3 更新id为3的发送json格式的用户对象
- DELETE 方式请求/api/user/4删除 ID为 4的user对象
- DELETE 方式请求/api/user/删除所有user

```

```

```java
package com.websystique.springmvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.websystique.springmvc.model.User;
import com.websystique.springmvc.service.UserService;

// Spring 4的新注解 @RestController注解.此注解避免了每个方法都要加上@ResponseBody注解
// 如果方法加上了@ResponseBody注解，Spring返回值到响应体。如果这样做的话，Spring将根据请求中的 Content-Type header（私下）使用 HTTP Message converters 来将domain对象转换为响应体
@RestController
public class HelloWorldRestController {

   @Autowired
   UserService userService;  //Service which will do all data retrieval/manipulation work

   
   //-------------------Retrieve All Users--------------------------------------------------------
   // GET 方式请求 /api/user/ 返回用户列表
   @RequestMapping(value = "/user/", method = RequestMethod.GET)
   public ResponseEntity<List<User>> listAllUsers() {
      List<User> users = userService.findAllUsers();
      if(users.isEmpty()){
         // ResponseEntity是一个真实数据.它代表了整个 HTTP 响应（response）. 它的好处是你可以控制任何对象放到它内部.你可以指定状态码、头信息和响应体.它包含你想要构建HTTP Response 的信息
         return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
      }
      return new ResponseEntity<List<User>>(users, HttpStatus.OK);
   }


   //-------------------Retrieve Single User--------------------------------------------------------
   // GET 方式请求 /api/user/1返回id为1的用户
   @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<User> getUser(@PathVariable("id") long id) {
      // @PathVariable注解意味着一个方法参数应该绑定到一个url模板变量[在'{}'里的一个]中
      System.out.println("Fetching User with id " + id);
      User user = userService.findById(id);
      if (user == null) {
         System.out.println("User with id " + id + " not found");
         return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<User>(user, HttpStatus.OK);
   }

   
   
   //-------------------Create a User--------------------------------------------------------
   // POST 方式请求 /api/user/ 通过user对象的JSON 参数创建新的user对象
   @RequestMapping(value = "/user/", method = RequestMethod.POST)
   public ResponseEntity<Void> createUser(@RequestBody User user,     UriComponentsBuilder ucBuilder) {
      System.out.println("Creating User " + user.getName());

      if (userService.isUserExist(user)) {
         System.out.println("A User with name " + user.getName() + " already exist");
         return new ResponseEntity<Void>(HttpStatus.CONFLICT);
      }

      userService.saveUser(user);

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
      return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
   }

   
   //------------------- Update a User --------------------------------------------------------
   // PUT 方式请求 /api/user/3 更新id为3的发送json格式的用户对象
   @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
   public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
      System.out.println("Updating User " + id);
      
      User currentUser = userService.findById(id);
      
      if (currentUser==null) {
         System.out.println("User with id " + id + " not found");
         return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
      }

      currentUser.setName(user.getName());
      currentUser.setAge(user.getAge());
      currentUser.setSalary(user.getSalary());
      
      userService.updateUser(currentUser);
      return new ResponseEntity<User>(currentUser, HttpStatus.OK);
   }

   //------------------- Delete a User --------------------------------------------------------
   // DELETE 方式请求/api/user/4删除 ID为 4的user对象
   @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
   public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
      System.out.println("Fetching & Deleting User with id " + id);

      User user = userService.findById(id);
      if (user == null) {
         System.out.println("Unable to delete. User with id " + id + " not found");
         return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
      }

      userService.deleteUserById(id);
      return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
   }

   
   //------------------- Delete All User --------------------------------------------------------
   // DELETE 方式请求/api/user/删除所有user
   @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
   public ResponseEntity<User> deleteAllUsers() {
      System.out.println("Deleting All Users");

      userService.deleteAllUsers();
      return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
   }

}
```

## springmvc注解详解

`@RestController` :首先我们使用的是Spring 4的新注解 @RestController注解.

此注解避免了每个方法都要加上@ResponseBody注解。也就是说@RestController 自己戴上了 @ResponseBody注解,看以看作是

`@RequestBody` : 如果方法参数被 @RequestBody注解，Spring将绑定HTTP请求体到那个参数上。如果那样做，Spring将根据请求中的ACCEPT或者 Content-Type header（私下）使用 HTTP Message converters 来将http请求体转化为domain对象。

`@ResponseBody` : 如果方法加上了@ResponseBody注解，Spring返回值到响应体。如果这样做的话，Spring将根据请求中的 Content-Type header（私下）使用 HTTP Message converters 来将domain对象转换为响应体。

`ResponseEntity`： 是一个真实数据.它代表了整个 HTTP 响应（response）. 它的好处是你可以控制任何对象放到它内部。

你可以指定状态码、头信息和响应体。它包含你想要构建HTTP Response 的信息。

`@PathVariable`： 此注解意味着一个方法参数应该绑定到一个url模板变量[在'{}'里的一个]中

一般来说你，要实现REST API in Spring 4 需要了解@RestController , @RequestBody, ResponseEntity 和 @PathVariable 这些注解 .另外, spring 也提供了一些支持类帮助你实现一些可定制化的东西。

`MediaType` : 带着 @RequestMapping 注解,通过特殊的控制器方法你可以额外指定,MediaType来生产或者消耗。

