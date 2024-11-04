package org.example.model;

import cn.hutool.json.JSONArray;
import com.domain.EpcSection;
import lombok.Data;

import java.util.Date;

@Data
public class EpcSectionDto {

    private String code;

    private String data;

    private String type;

    private String image;

    private String label;

    private JSONArray notes;

    private String folder;

    private Date enddate;

    private String comments;

    private JSONArray sections;

    private String uniqueid;

    private Date begindate;

    private String imagepath;

    private String applicable;

    private Date dateformat;

    private String diagnostic;

    private String objecttype;

    private String catalogcode;

    private String parentindex;

    private JSONArray childrenindex;

    private String parentuniqueid;

    private String additionallabel;

    private JSONArray largeimagepaths;

    private String showsectionimage;

    private JSONArray notesreferencekeys;

    private String thumbnailimagepath;

    private String featuredescriptions;

}
