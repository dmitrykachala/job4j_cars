package ru.job4j.cars.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import ru.job4j.cars.model.Ad;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Category;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.AdRepository;
import ru.job4j.cars.repository.CarRepository;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();
    private static final File STORAGE = new File("c:/images/cars");
    private static final List<String> ALLOWED_EXTANSION = new ArrayList<>();

    static {
        ALLOWED_EXTANSION.add("jpg");
        ALLOWED_EXTANSION.add("jpeg");
        ALLOWED_EXTANSION.add("gif");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        AdRepository store = AdRepository.getInstance();
        CarRepository cStore = CarRepository.getInstance();

        Response response = new Response();
        response.ads = store.findAll();
        response.categories = store.allCategories();
        response.cars = cStore.findAll();

        response.user = (User) req.getSession().getAttribute("user");

        String json = GSON.toJson(response);

        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        List<String> cIds = new ArrayList<>();
        int carId = 0;
        AdRepository store = AdRepository.getInstance();

        Ad ad = new Ad();

        ad.setUser((User) req.getSession().getAttribute("user"));
        ad.setCreated(new Date(System.currentTimeMillis()));
        ad.setSold(false);

        DiskFileItemFactory factory = newDiskFileItemFactory(getServletContext(),
                (File) getServletContext().getAttribute("javax.servlet.context.tempdir"));

        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List<FileItem> items = upload.parseRequest(req);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    if (item.getFieldName().equals("cCars")) {
                        carId = Integer.parseInt(item.getString("UTF-8"));
                    } else if (item.getFieldName().equals("cIds")) {
                        cIds.add(item.getString("UTF-8"));
                    } else if (item.getFieldName().equals("description")) {
                        ad.setDescription(item.getString("UTF-8"));
                    }
                } else {
                    /*String fieldName = item.getFieldName();*/
                    String fileName = item.getName();
                    /*String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();*/

                    if (ALLOWED_EXTANSION.contains(fileName.substring(fileName
                            .lastIndexOf(".") + 1))) {
                        File uploadedFile = new File(STORAGE,
                                ((int) (Math.random() * 10000)) + "-" + fileName);
                        item.write(uploadedFile);
                        ad.setPictureLink(uploadedFile.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        store.addNewAd(ad, cIds.toArray(new String[]{}), carId);

        resp.sendRedirect(req.getContextPath());

    }

    public class Response {
        private List<Ad> ads;
        private List<Category> categories;
        private List<Car> cars;
        private User user;

        public List<Ad> getAds() {
            return ads;
        }

        public void setAds(List<Ad> ads) {
            this.ads = ads;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<Car> getCars() {
            return cars;
        }

        public void setCars(List<Car> cars) {
            this.cars = cars;
        }

        public User getUser() {
            return user;
        }

        public void setUserName(User user) {
            this.user = user;
        }
    }

    public static DiskFileItemFactory newDiskFileItemFactory(ServletContext context,
                                                             File repository) {
        FileCleaningTracker fileCleaningTracker
                = FileCleanerCleanup.getFileCleaningTracker(context);
        DiskFileItemFactory factory
                = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
        factory.setFileCleaningTracker(fileCleaningTracker);
        return factory;
    }
}
