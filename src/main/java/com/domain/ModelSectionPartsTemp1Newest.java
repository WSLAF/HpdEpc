package com.domain;

import org.example.model.SectionPartNewDto;
import org.springframework.beans.BeanUtils;

public class ModelSectionPartsTemp1Newest {
    private Long id;

    private String catalogCode;

    private String sectionCode;

    private String applicable;

    private String beginDate;

    private String callout;

    private String data;

    private String endDate;

    private String hasImage;

    private String label;

    private String automakerLabel;

    private String qty;

    private String remark;

    private String type;

    private String sourceType;

    private Boolean hasNotes;

    private String userNote;

    private String pdfLinks;

    private Boolean hasTSB;

    private Boolean hasAccNote;

    private Boolean hasPANT;

    private Boolean isTranslatePart;

    private Boolean hasHistoricalParts;

    private Boolean selectiveFit;

    private String partSequenceNo;

    private String brand;

    private Integer selectiveFitGroupId;

    private String avsCodes;

    private String modelCodes;

    private String revisionRef;

    private String labelLanguage;

    private String notes;

    private String price;

    private String lookups;

    private String fitmentInstructions;

    private String clientIdentificationData;

    private String sections;

    private String basePart;

    private String notesReferenceKeys;

    private Integer jwfPartMasterNextPartNumber;

    private Boolean openPartPrice;

    private String applicability;

    private String partNumber;

    private String optionIds;

//    public static SectionPart convert(SectionPartDto dto) {
//        SectionPart sectionPart = new SectionPart();
//        BeanUtils.copyProperties(dto, sectionPart);
//        sectionPart.setUserNote(dto.getUserNote().toJSONString(2));
//        sectionPart.setNotes(dto.getNotes().toJSONString(2));
//        sectionPart.setPrice(dto.getPrice().toJSONString(2));
//        sectionPart.setLookups(dto.getLookups().toJSONString(2));
//        sectionPart.setClientIdentificationData(dto.getClientIdentificationData().toJSONString(2));
//        sectionPart.setSections(dto.getSections().toJSONString(2));
//        sectionPart.setNotesReferenceKeys(dto.getNotesReferenceKeys().toJSONString(2));
//        sectionPart.setOptionId(dto.getOptionId());
//        return sectionPart;
//    }

    public static ModelSectionPartsTemp1Newest convert(SectionPartNewDto dto) {
        ModelSectionPartsTemp1Newest modelSectionPartsTemp1 = new ModelSectionPartsTemp1Newest();
        BeanUtils.copyProperties(dto, modelSectionPartsTemp1);
        modelSectionPartsTemp1.setUserNote(dto.getUserNote().toJSONString(2));
        modelSectionPartsTemp1.setNotes(dto.getNotes().toJSONString(2));
        modelSectionPartsTemp1.setPrice(dto.getPrice().toJSONString(2));
        modelSectionPartsTemp1.setLookups(dto.getLookups().toJSONString(2));
        modelSectionPartsTemp1.setClientIdentificationData(dto.getClientIdentificationData().toJSONString(2));
        modelSectionPartsTemp1.setSections(dto.getSections().toJSONString(2));
        modelSectionPartsTemp1.setNotesReferenceKeys(dto.getNotesReferenceKeys().toJSONString(2));
        modelSectionPartsTemp1.setOptionIds(dto.getOptionIds().toJSONString(2));
        return modelSectionPartsTemp1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getApplicable() {
        return applicable;
    }

    public void setApplicable(String applicable) {
        this.applicable = applicable;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getCallout() {
        return callout;
    }

    public void setCallout(String callout) {
        this.callout = callout;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHasImage() {
        return hasImage;
    }

    public void setHasImage(String hasImage) {
        this.hasImage = hasImage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAutomakerLabel() {
        return automakerLabel;
    }

    public void setAutomakerLabel(String automakerLabel) {
        this.automakerLabel = automakerLabel;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Boolean getHasNotes() {
        return hasNotes;
    }

    public void setHasNotes(Boolean hasNotes) {
        this.hasNotes = hasNotes;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public String getPdfLinks() {
        return pdfLinks;
    }

    public void setPdfLinks(String pdfLinks) {
        this.pdfLinks = pdfLinks;
    }

    public Boolean getHasTSB() {
        return hasTSB;
    }

    public void setHasTSB(Boolean hasTSB) {
        this.hasTSB = hasTSB;
    }

    public Boolean getHasAccNote() {
        return hasAccNote;
    }

    public void setHasAccNote(Boolean hasAccNote) {
        this.hasAccNote = hasAccNote;
    }

    public Boolean getHasPANT() {
        return hasPANT;
    }

    public void setHasPANT(Boolean hasPANT) {
        this.hasPANT = hasPANT;
    }

    public Boolean getIsTranslatePart() {
        return isTranslatePart;
    }

    public void setIsTranslatePart(Boolean isTranslatePart) {
        this.isTranslatePart = isTranslatePart;
    }

    public Boolean getHasHistoricalParts() {
        return hasHistoricalParts;
    }

    public void setHasHistoricalParts(Boolean hasHistoricalParts) {
        this.hasHistoricalParts = hasHistoricalParts;
    }

    public Boolean getSelectiveFit() {
        return selectiveFit;
    }

    public void setSelectiveFit(Boolean selectiveFit) {
        this.selectiveFit = selectiveFit;
    }

    public String getPartSequenceNo() {
        return partSequenceNo;
    }

    public void setPartSequenceNo(String partSequenceNo) {
        this.partSequenceNo = partSequenceNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getSelectiveFitGroupId() {
        return selectiveFitGroupId;
    }

    public void setSelectiveFitGroupId(Integer selectiveFitGroupId) {
        this.selectiveFitGroupId = selectiveFitGroupId;
    }

    public String getAvsCodes() {
        return avsCodes;
    }

    public void setAvsCodes(String avsCodes) {
        this.avsCodes = avsCodes;
    }

    public String getModelCodes() {
        return modelCodes;
    }

    public void setModelCodes(String modelCodes) {
        this.modelCodes = modelCodes;
    }

    public String getRevisionRef() {
        return revisionRef;
    }

    public void setRevisionRef(String revisionRef) {
        this.revisionRef = revisionRef;
    }

    public String getLabelLanguage() {
        return labelLanguage;
    }

    public void setLabelLanguage(String labelLanguage) {
        this.labelLanguage = labelLanguage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLookups() {
        return lookups;
    }

    public void setLookups(String lookups) {
        this.lookups = lookups;
    }

    public String getFitmentInstructions() {
        return fitmentInstructions;
    }

    public void setFitmentInstructions(String fitmentInstructions) {
        this.fitmentInstructions = fitmentInstructions;
    }

    public String getClientIdentificationData() {
        return clientIdentificationData;
    }

    public void setClientIdentificationData(String clientIdentificationData) {
        this.clientIdentificationData = clientIdentificationData;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public String getBasePart() {
        return basePart;
    }

    public void setBasePart(String basePart) {
        this.basePart = basePart;
    }

    public String getNotesReferenceKeys() {
        return notesReferenceKeys;
    }

    public void setNotesReferenceKeys(String notesReferenceKeys) {
        this.notesReferenceKeys = notesReferenceKeys;
    }

    public Integer getJwfPartMasterNextPartNumber() {
        return jwfPartMasterNextPartNumber;
    }

    public void setJwfPartMasterNextPartNumber(Integer jwfPartMasterNextPartNumber) {
        this.jwfPartMasterNextPartNumber = jwfPartMasterNextPartNumber;
    }

    public Boolean getOpenPartPrice() {
        return openPartPrice;
    }

    public void setOpenPartPrice(Boolean openPartPrice) {
        this.openPartPrice = openPartPrice;
    }

    public String getApplicability() {
        return applicability;
    }

    public void setApplicability(String applicability) {
        this.applicability = applicability;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(String optionIds) {
        this.optionIds = optionIds;
    }
}