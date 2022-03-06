package com.zhang.forum.controller;

import com.zhang.forum.entity.User;
import com.zhang.forum.service.AlphaService;
import com.zhang.forum.service.UserService;
import com.zhang.forum.util.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private UserService userService;
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }

    @RequestMapping("/hello2")
    @ResponseBody
    public String sayHello2() {
        System.out.println(alphaService.find());
        return "success";
    }

    @RequestMapping("/data")
    @ResponseBody
    public User getData() {
        User user = userService.findUserById(1);
        return user;
    }

    @RequestMapping("/http")
    // 一般来说不加ResponseBody，return会默认响应html数据，加了就会在浏览器响应字符串
    //为什么此时没有@ResponseBody呢，因为可以通过HttpServletResponse向浏览器输出对象，也就是说HttpServletResponse代替了@ResponseBody
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        // 获取请求方式
        System.out.println(request.getMethod());
        // 获取请求路径
        System.out.println(request.getServletPath());
        // 获取请求行：得到所有请求行的key，比如accept-language ,user-agent
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }

        //获取请求体
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        // 返回数据的类型：比如返回一个网页text/html，字符集是utf-8
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write("<h1>porum</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    // GET请求怎么处理

    // 传进来http://localhost:8080/forum/alpha/students?current=133&limit=2056
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody

    //万一请求路径没带参数(?current=1&limit=20)，需要@RequestParam，设定默认值
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // 当参数成为路径的一部分，需要怎么获取呢，需要用到@PathVariable
    // 比如http://localhost:8080/forum/alpha/student/13
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }


    // 如何响应POST请求,需要造一个表单，表单是以post方式提交  http://localhost:8080/forum/html/student.html
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 服务器将HTML数据返回给浏览器，无需加@ResponseBody
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        //注意要在pom中配置thymeleaf,否则加载不出来
        ModelAndView mav = new ModelAndView();
        System.out.println("进来了");
        mav.addObject("name", "张三");
        mav.addObject("age", 30);
        //返回templates/demo/view.html,还要修改application.properties的静态资源访问路径
        mav.setViewName("/demo/view");
//        mav.setViewName("/demo/view.html");
        return mav;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "北京大学");
        model.addAttribute("age", 80);
        return "/demo/view";
    }

    // 服务器响应JSON数据(通常是在异步请求中相应json数据)
    // 服务器把Java对象数据返回给浏览器 -> java对象转为JSON字符串给浏览器传过去 -> 浏览器将json数据转为JS对象
    // 不加ResponseBody，会默认响应html数据，加了就会响应字符串
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.00);
        return emp;
    }

    // 响应一个List集合
    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "李四");
        emp.put("age", 24);
        emp.put("salary", 9000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "王五");
        emp.put("age", 25);
        emp.put("salary", 10000.00);
        list.add(emp);

        return list;
        //在http://localhost:8080/forum/alpha/emps中会显示[{"name":"张三","salary":8000.0,"age":23},{"name":"李四","salary":9000.0,"age":24},{"name":"王五","salary":10000.0,"age":25}]
    }

    // cookie示例，往cookie里存值
    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建cookie
        Cookie cookie = new Cookie("code", ForumUtil.generateUUID());
        // 设置cookie生效的范围
        cookie.setPath("/forum/alpha");
        // 设置cookie的生存时间
        cookie.setMaxAge(60 * 10);
        // 发送cookie
        response.addCookie(cookie);

        return "set cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }

    // session示例

    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

//     ajax示例
    //点击http://localhost:8080/forum/html/ajax-demo.html
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age) {
        System.out.println(name);//浏览器向服务器提交的数据
        System.out.println(age);
        String jsonString = ForumUtil.getJSONString(0, "操作成功!");
        System.out.println(jsonString);
        return jsonString;//服务器向浏览器返回的数据，在ajax-demo的js里用console输出
    }
}
