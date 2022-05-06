package servlets;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import daos.UserDao;
import daos.UserDaoImpl;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name="userServlet", value="/users")
public class UserServlet extends HttpServlet {
    // Here manually creating a user dao implementation instance:
    private UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Using the UserDao class instance(object) to call(use) the methods to communicate with the database:
            List<User> users = userDao.getAll();

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(users);

            resp.setStatus(200);
            resp.getWriter().print(json);
        }catch(IOException ex) {
            resp.setStatus(500);
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Get user input from the request object and create a json (string) payload object of User type:
            User payload = mapper.readValue(req.getInputStream(), User.class);
            // Tell userDao implementation to insert the user in db:
            userDao.create(payload);
            // Setting response status code
            resp.setStatus(203);
            resp.getWriter().print("User successfully added");
        }catch (IOException ex) {
            resp.setStatus(500);
            resp.getWriter().print("Something went wrong creating your profile!");
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            User payload = mapper.readValue(req.getInputStream(), User.class);
            userDao.update(payload);

            resp.setStatus(200);
            resp.getWriter().print("Record successfully updated!");

        }catch (IOException ex) {
            resp.setStatus(500);
            resp.getWriter().print("Something went wrong updating your record!");
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream string_id = req.getInputStream();
        System.out.println(string_id);
        try {
            ObjectMapper mapper = new ObjectMapper();
            int user_id = mapper.readValue(req.getInputStream(), Integer.class);
            System.out.println(user_id);
            userDao.delete(user_id);
            resp.setStatus(204);
            resp.getWriter().print("User has been deleted!");
        }catch (IOException ex) {
            resp.setStatus(500);
            resp.getWriter().print("Something went wrong deleting your record!");
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
