package com.domain;

import org.example.model.EpcSectionDto;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class EpcSection {
    private String code;

    private String data;

    private String type;

    private String image;

    private String label;

    private String notes;

    private String folder;

    private Date enddate;

    private String comments;

    private String sections;

    private String uniqueid;

    private Date begindate;

    private String imagepath;

    private String applicable;

    private Date dateformat;

    private String diagnostic;

    private String objecttype;

    private String catalogcode;

    private String parentindex;

    private String childrenindex;

    private String parentuniqueid;

    private String additionallabel;

    private String largeimagepaths;

    private String showsectionimage;

    private String notesreferencekeys;

    private String thumbnailimagepath;

    private String featuredescriptions;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getApplicable() {
        return applicable;
    }

    public void setApplicable(String applicable) {
        this.applicable = applicable;
    }

    public Date getDateformat() {
        return dateformat;
    }

    public void setDateformat(Date dateformat) {
        this.dateformat = dateformat;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }

    public String getCatalogcode() {
        return catalogcode;
    }

    public void setCatalogcode(String catalogcode) {
        this.catalogcode = catalogcode;
    }

    public String getParentindex() {
        return parentindex;
    }

    public void setParentindex(String parentindex) {
        this.parentindex = parentindex;
    }

    public String getChildrenindex() {
        return childrenindex;
    }

    public void setChildrenindex(String childrenindex) {
        this.childrenindex = childrenindex;
    }

    public String getParentuniqueid() {
        return parentuniqueid;
    }

    public void setParentuniqueid(String parentuniqueid) {
        this.parentuniqueid = parentuniqueid;
    }

    public String getAdditionallabel() {
        return additionallabel;
    }

    public void setAdditionallabel(String additionallabel) {
        this.additionallabel = additionallabel;
    }

    public String getLargeimagepaths() {
        return largeimagepaths;
    }

    public void setLargeimagepaths(String largeimagepaths) {
        this.largeimagepaths = largeimagepaths;
    }

    public String getShowsectionimage() {
        return showsectionimage;
    }

    public void setShowsectionimage(String showsectionimage) {
        this.showsectionimage = showsectionimage;
    }

    public String getNotesreferencekeys() {
        return notesreferencekeys;
    }

    public void setNotesreferencekeys(String notesreferencekeys) {
        this.notesreferencekeys = notesreferencekeys;
    }

    public String getThumbnailimagepath() {
        return thumbnailimagepath;
    }

    public void setThumbnailimagepath(String thumbnailimagepath) {
        this.thumbnailimagepath = thumbnailimagepath;
    }

    public String getFeaturedescriptions() {
        return featuredescriptions;
    }

    public void setFeaturedescriptions(String featuredescriptions) {
        this.featuredescriptions = featuredescriptions;
    }

    public static EpcSection convert(EpcSectionDto dto) {
        EpcSection section = new EpcSection();
        BeanUtils.copyProperties(dto, section);
        section.setChildrenindex(dto.getChildrenindex().toJSONString(2));
        section.setNotesreferencekeys(dto.getNotesreferencekeys().toJSONString(2));
        section.setLargeimagepaths(dto.getLargeimagepaths().toJSONString(2));
        section.setNotes(dto.getNotes().toJSONString(2));
        section.setSections(dto.getSections().toJSONString(2));
        return section;
    }
}