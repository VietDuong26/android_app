package com.example.shoesstore.config.cloudinary;

import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudinaryManager {
    private static final String TAG = "CloudinaryManager";
    private Cloudinary cloudinary;

    public CloudinaryManager() {
        Map config = new HashMap();
        config.put("cloud_name", "vietduong26");
        config.put("api_key", "889174765855846");
        config.put("api_secret", "1_7TxL0FmooXS4oyMTaQ8FfJ4t0");

        cloudinary = new Cloudinary(config);
    }

    public String uploadImage(File imageFile, String publicId) {
        try {
            Map map = ObjectUtils.asMap("public_id", publicId, "overwrite", true);
            String url = (String) cloudinary.uploader().upload(imageFile, map).get("secure_url");
            return url;
        } catch (Exception e) {
            Log.e(TAG, "Error uploading image to Cloudinary: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void deleteAllImageByProductId(int productId) throws Exception {
        String folder = "product/" + productId;
        Map result = cloudinary.api().resources(ObjectUtils.asMap(
                "type", "upload",
                "prefix", folder + "/"
        ));
        List<Map> resources = (List<Map>) result.get("resources");
        List<String> publicIds = new ArrayList<>();
        for (Map resource : resources) {
            publicIds.add((String) resource.get("public_id"));
        }
        if (!publicIds.isEmpty()) {
            Map deleteResult = cloudinary.api().deleteResources(publicIds, ObjectUtils.emptyMap());
            System.out.println(deleteResult);
        }
    }
}
