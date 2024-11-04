//package com.domain;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import org.apache.ibatis.annotations.Options;
//import org.example.model.SectionPartDto;
//import org.springframework.beans.BeanUtils;
//
//public class SectionPartsTempSqlserver {
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    @TableId
//    private Long id;
//
//    @TableField("catalogCode")
//
//    private String catalogCode;
//
//    @TableField("sectionCode")
//    private String sectionCode;
//
//    private String applicable;
//
//    @TableField("beginDate")
//    private String beginDate;
//
//    private String callout;
//
//    private String data;
//
//    @TableField("endDate")
//    private String endDate;
//
//    @TableField("hasImage")
//    private String hasImage;
//
//    private String label;
//
//    @TableField("automakerLabel")
//    private String automakerLabel;
//
//    private String qty;
//
//    private String remark;
//
//    private String type;
//
//    @TableField("sourceType")
//    private String sourceType;
//
//    @TableField("hasNotes")
//    private String hasNotes;
//
//    @TableField("userNote")
//    private String userNote;
//
//    @TableField("pdfLinks")
//    private String pdfLinks;
//
//    @TableField("hasTSB")
//    private String hasTSB;
//
//    @TableField("hasAccNote")
//    private String hasAccNote;
//
//    @TableField("hasPANT")
//    private String hasPANT;
//
//    @TableField("isTranslatePart")
//    private String isTranslatePart;
//
//    @TableField("hasHistoricalParts")
//    private String hasHistoricalParts;
//
//    @TableField("selectiveFit")
//    private String selectiveFit;
//
//    @TableField("partSequenceNo")
//    private String partSequenceNo;
//
//    private String brand;
//
//    @TableField("selectiveFitGroupId")
//    private Integer selectiveFitGroupId;
//
//    @TableField("avsCodes")
//    private String avsCodes;
//
//    @TableField("modelCodes")
//    private String modelCodes;
//
//    @TableField("revisionRef")
//    private String revisionRef;
//
//    @TableField("labelLanguage")
//    private String labelLanguage;
//
//    private String notes;
//
//    private String price;
//
//    private String lookups;
//
//    @TableField("fitmentInstructions")
//    private String fitmentInstructions;
//
//    @TableField("clientIdentificationData")
//    private String clientIdentificationData;
//
//    private String sections;
//
//    @TableField("basePart")
//    private String basePart;
//
//    @TableField("notesReferenceKeys")
//    private String notesReferenceKeys;
//
//    @TableField("jwfPartMasterNextPartNumber")
//    private Integer jwfPartMasterNextPartNumber;
//
//    @TableField("openPartPrice")
//    private String openPartPrice;
//    private String applicability;
//
//    @TableField("partNumber")
//    private String partNumber;
//
//    public String getCatalogCode() {
//        return catalogCode;
//    }
//
//    public void setCatalogCode(String catalogCode) {
//        this.catalogCode = catalogCode;
//    }
//
//    public String getSectionCode() {
//        return sectionCode;
//    }
//
//    public void setSectionCode(String sectionCode) {
//        this.sectionCode = sectionCode;
//    }
//
//    public String getApplicable() {
//        return applicable;
//    }
//
//    public void setApplicable(String applicable) {
//        this.applicable = applicable;
//    }
//
//    public String getBeginDate() {
//        return beginDate;
//    }
//
//    public void setBeginDate(String beginDate) {
//        this.beginDate = beginDate;
//    }
//
//    public String getCallout() {
//        return callout;
//    }
//
//    public void setCallout(String callout) {
//        this.callout = callout;
//    }
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
//
//    public String getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(String endDate) {
//        this.endDate = endDate;
//    }
//
//    public String getHasImage() {
//        return hasImage;
//    }
//
//    public void setHasImage(String hasImage) {
//        this.hasImage = hasImage;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    public String getAutomakerLabel() {
//        return automakerLabel;
//    }
//
//    public void setAutomakerLabel(String automakerLabel) {
//        this.automakerLabel = automakerLabel;
//    }
//
//    public String getQty() {
//        return qty;
//    }
//
//    public void setQty(String qty) {
//        this.qty = qty;
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getSourceType() {
//        return sourceType;
//    }
//
//    public void setSourceType(String sourceType) {
//        this.sourceType = sourceType;
//    }
//
//    public String getHasNotes() {
//        return hasNotes;
//    }
//
//    public void setHasNotes(String hasNotes) {
//        this.hasNotes = hasNotes;
//    }
//
//    public String getUserNote() {
//        return userNote;
//    }
//
//    public void setUserNote(String userNote) {
//        this.userNote = userNote;
//    }
//
//    public String getPdfLinks() {
//        return pdfLinks;
//    }
//
//    public void setPdfLinks(String pdfLinks) {
//        this.pdfLinks = pdfLinks;
//    }
//
//    public String getHasTSB() {
//        return hasTSB;
//    }
//
//    public void setHasTSB(String hasTSB) {
//        this.hasTSB = hasTSB;
//    }
//
//    public String getHasAccNote() {
//        return hasAccNote;
//    }
//
//    public void setHasAccNote(String hasAccNote) {
//        this.hasAccNote = hasAccNote;
//    }
//
//    public String getHasPANT() {
//        return hasPANT;
//    }
//
//    public void setHasPANT(String hasPANT) {
//        this.hasPANT = hasPANT;
//    }
//
//    public String getIsTranslatePart() {
//        return isTranslatePart;
//    }
//
//    public void setIsTranslatePart(String isTranslatePart) {
//        this.isTranslatePart = isTranslatePart;
//    }
//
//    public String getHasHistoricalParts() {
//        return hasHistoricalParts;
//    }
//
//    public void setHasHistoricalParts(String hasHistoricalParts) {
//        this.hasHistoricalParts = hasHistoricalParts;
//    }
//
//    public String getSelectiveFit() {
//        return selectiveFit;
//    }
//
//    public void setSelectiveFit(String selectiveFit) {
//        this.selectiveFit = selectiveFit;
//    }
//
//    public String getPartSequenceNo() {
//        return partSequenceNo;
//    }
//
//    public void setPartSequenceNo(String partSequenceNo) {
//        this.partSequenceNo = partSequenceNo;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public Integer getSelectiveFitGroupId() {
//        return selectiveFitGroupId;
//    }
//
//    public void setSelectiveFitGroupId(Integer selectiveFitGroupId) {
//        this.selectiveFitGroupId = selectiveFitGroupId;
//    }
//
//    public String getAvsCodes() {
//        return avsCodes;
//    }
//
//    public void setAvsCodes(String avsCodes) {
//        this.avsCodes = avsCodes;
//    }
//
//    public String getModelCodes() {
//        return modelCodes;
//    }
//
//    public void setModelCodes(String modelCodes) {
//        this.modelCodes = modelCodes;
//    }
//
//    public String getRevisionRef() {
//        return revisionRef;
//    }
//
//    public void setRevisionRef(String revisionRef) {
//        this.revisionRef = revisionRef;
//    }
//
//    public String getLabelLanguage() {
//        return labelLanguage;
//    }
//
//    public void setLabelLanguage(String labelLanguage) {
//        this.labelLanguage = labelLanguage;
//    }
//
//    public String getNotes() {
//        return notes;
//    }
//
//    public void setNotes(String notes) {
//        this.notes = notes;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public String getLookups() {
//        return lookups;
//    }
//
//    public void setLookups(String lookups) {
//        this.lookups = lookups;
//    }
//
//    public String getFitmentInstructions() {
//        return fitmentInstructions;
//    }
//
//    public void setFitmentInstructions(String fitmentInstructions) {
//        this.fitmentInstructions = fitmentInstructions;
//    }
//
//    public String getClientIdentificationData() {
//        return clientIdentificationData;
//    }
//
//    public void setClientIdentificationData(String clientIdentificationData) {
//        this.clientIdentificationData = clientIdentificationData;
//    }
//
//    public String getSections() {
//        return sections;
//    }
//
//    public void setSections(String sections) {
//        this.sections = sections;
//    }
//
//    public String getBasePart() {
//        return basePart;
//    }
//
//    public void setBasePart(String basePart) {
//        this.basePart = basePart;
//    }
//
//    public String getNotesReferenceKeys() {
//        return notesReferenceKeys;
//    }
//
//    public void setNotesReferenceKeys(String notesReferenceKeys) {
//        this.notesReferenceKeys = notesReferenceKeys;
//    }
//
//    public Integer getJwfPartMasterNextPartNumber() {
//        return jwfPartMasterNextPartNumber;
//    }
//
//    public void setJwfPartMasterNextPartNumber(Integer jwfPartMasterNextPartNumber) {
//        this.jwfPartMasterNextPartNumber = jwfPartMasterNextPartNumber;
//    }
//
//    public String getOpenPartPrice() {
//        return openPartPrice;
//    }
//
//    public void setOpenPartPrice(String openPartPrice) {
//        this.openPartPrice = openPartPrice;
//    }
//
//    public String getApplicability() {
//        return applicability;
//    }
//
//    public void setApplicability(String applicability) {
//        this.applicability = applicability;
//    }
//
//    public String getPartNumber() {
//        return partNumber;
//    }
//
//    public void setPartNumber(String partNumber) {
//        this.partNumber = partNumber;
//    }
//
//    public static SectionPartsTempSqlserver convert(SectionPartDto dto) {
//        SectionPartsTempSqlserver sectionPart = new SectionPartsTempSqlserver();
//        BeanUtils.copyProperties(dto, sectionPart);
//        sectionPart.setUserNote(dto.getUserNote().toJSONString(2));
//        sectionPart.setNotes(dto.getNotes().toJSONString(2));
//        sectionPart.setPrice(dto.getPrice().toJSONString(2));
//        sectionPart.setLookups(dto.getLookups().toJSONString(2));
//        sectionPart.setClientIdentificationData(dto.getClientIdentificationData().toJSONString(2));
//        sectionPart.setSections(dto.getSections().toJSONString(2));
//        sectionPart.setNotesReferenceKeys(dto.getNotesReferenceKeys().toJSONString(2));
//        return sectionPart;
//    }
//
//    @Override
//    public String toString() {
//        return "SectionPartsTempSqlserver{" +
//                "catalogCode='" + catalogCode + '\'' +
//                ", sectionCode='" + sectionCode + '\'' +
//                ", applicable='" + applicable + '\'' +
//                ", beginDate='" + beginDate + '\'' +
//                ", callout='" + callout + '\'' +
//                ", data='" + data + '\'' +
//                ", endDate='" + endDate + '\'' +
//                ", hasImage='" + hasImage + '\'' +
//                ", label='" + label + '\'' +
//                ", automakerLabel='" + automakerLabel + '\'' +
//                ", qty='" + qty + '\'' +
//                ", remark='" + remark + '\'' +
//                ", type='" + type + '\'' +
//                ", sourceType='" + sourceType + '\'' +
//                ", hasNotes='" + hasNotes + '\'' +
//                ", userNote='" + userNote + '\'' +
//                ", pdfLinks='" + pdfLinks + '\'' +
//                ", hasTSB='" + hasTSB + '\'' +
//                ", hasAccNote='" + hasAccNote + '\'' +
//                ", hasPANT='" + hasPANT + '\'' +
//                ", isTranslatePart='" + isTranslatePart + '\'' +
//                ", hasHistoricalParts='" + hasHistoricalParts + '\'' +
//                ", selectiveFit='" + selectiveFit + '\'' +
//                ", partSequenceNo='" + partSequenceNo + '\'' +
//                ", brand='" + brand + '\'' +
//                ", selectiveFitGroupId=" + selectiveFitGroupId +
//                ", avsCodes='" + avsCodes + '\'' +
//                ", modelCodes='" + modelCodes + '\'' +
//                ", revisionRef='" + revisionRef + '\'' +
//                ", labelLanguage='" + labelLanguage + '\'' +
//                ", notes='" + notes + '\'' +
//                ", price='" + price + '\'' +
//                ", lookups='" + lookups + '\'' +
//                ", fitmentInstructions='" + fitmentInstructions + '\'' +
//                ", clientIdentificationData='" + clientIdentificationData + '\'' +
//                ", sections='" + sections + '\'' +
//                ", basePart='" + basePart + '\'' +
//                ", notesReferenceKeys='" + notesReferenceKeys + '\'' +
//                ", jwfPartMasterNextPartNumber=" + jwfPartMasterNextPartNumber +
//                ", openPartPrice='" + openPartPrice + '\'' +
//                ", applicability='" + applicability + '\'' +
//                ", partNumber='" + partNumber + '\'' +
//                '}';
//    }
//}