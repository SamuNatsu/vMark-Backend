package com.vmark.backend;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.InputStream;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class VMarkBackendApplication {
    private static SqlSession sqlSession;
    public static void main(String[] args) {
        // Initialize


        SpringApplication.run(VMarkBackendApplication.class, args);
    }

    // Initialize MyBatis
    private static void initMyBatis() {
        try (InputStream sin =
                     VMarkBackendApplication.class
                             .getClassLoader()
                             .getResourceAsStream("MyBatisConfig.xml")) {
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            SqlSessionFactory factory = builder.build(sin);
            sqlSession = factory.openSession();
        }
        catch (Exception e) {
            sqlSession = null;
        }
    }

}
