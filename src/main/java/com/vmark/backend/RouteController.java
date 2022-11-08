package com.vmark.backend;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

import com.vmark.backend.dao.UserDAO;
import com.vmark.backend.dao.UserInfo;
import java.util.List;

@RestController
public class RouteController {
    @RequestMapping("/auth")
    public String auth() {
        try {
            InputStream sin = RouteController.class.getClassLoader().getResourceAsStream("MyBatisConfig.xml");

            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            SqlSessionFactory factory = builder.build(sin);
            SqlSession session = factory.openSession();
            UserDAO dao = session.getMapper(UserDAO.class);
            List<UserInfo> ls = dao.findAll();
            return ls.toString();
        }
        catch (Exception e) {
            return e.toString();
        }
    }
}
