/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.magenta.jsonpersist;

import java.io.File;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.VersionLoggerListener;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 *
 * @author martin
 */
public class StartEmbeddedTomcat {
//
//    public static void main(String[] args) throws Exception {
//
//        String webappDirLocation = "src/main/webapp/";
//        Tomcat tomcat = new Tomcat();
//
//        //The port that we should run on can be set into an environment variable
//        //Look for that variable and default to 8080 if it isn't there.
//
//
//        tomcat.setPort(8080);
//
//        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
//        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
//
//        // Declare an alternative location for your "WEB-INF/classes" dir
//        // Servlet 3.0 annotation will work
//        File additionWebInfClasses = new File("target/classes");
//        WebResourceRoot resources = new StandardRoot(ctx);
//        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
//                additionWebInfClasses.getAbsolutePath(), "/"));
//        ctx.setResources(resources);
//
//        tomcat.start();
//        //tomcat.getServer().await();
//        System.in.read();
//    }

//    public static void main(String[] args) throws Exception {
//        File catalinaHome = new File("target/catalina"); // folder must exist
//        catalinaHome.mkdirs();
//        Tomcat tomcat = new Tomcat();
//        tomcat.setPort(8080); // HTTP port
//        tomcat.setBaseDir(catalinaHome.getAbsolutePath());
//        tomcat.getServer().addLifecycleListener(new VersionLoggerListener());
//        File war = new File("target/rest.war");
//        tomcat.addWebapp("/service", war.getAbsolutePath());
//        
//        File contextPath = new File(catalinaHome, "webapps/contextPath");
//        contextPath.mkdirs();
//        
//        tomcat.start();
//        System.in.read();
//    }
    public static void main(String[] args) throws ServletException, LifecycleException {
        String contextPath = "/";
        String webappDir = new File("target/rest").getAbsolutePath();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("target/catalina");
        tomcat.setPort(8080);
        tomcat.getConnector();

        tomcat.addWebapp(contextPath, webappDir);

        tomcat.start();
        tomcat.getServer().await();

    }

}
