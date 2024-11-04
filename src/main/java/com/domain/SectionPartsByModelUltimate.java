package com.domain;

import com.google.gson.JsonArray;
import lombok.Data;
import lombok.ToString;
import org.example.model.SectionPartDto;
import org.springframework.beans.BeanUtils;

@Data
@ToString
public class SectionPartsByModelUltimate {

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

    //todo 尝试一下
    private String optionIds;

    private Long originalId;

    private JsonArray optionIdsArr;

    public static SectionPartsByModelUltimate convert(SectionPartDto dto) {
        SectionPartsByModelUltimate sectionPart = new SectionPartsByModelUltimate();
        BeanUtils.copyProperties(dto, sectionPart);
        if (dto.getOptionId() != null) {
            JsonArray arr = new JsonArray();
            arr.add(dto.getOptionId());
            sectionPart.setOptionIdsArr(arr);
        }
        if (dto.getId() != null) {
            sectionPart.setOriginalId(dto.getId());
        }
        if (dto.getUserNote2() != null) {
            sectionPart.setUserNote(dto.getUserNote2());
        }
        if (dto.getNotes2() != null) {
            sectionPart.setNotes(dto.getNotes2());
        }
        if (dto.getPrice2() != null) {
            sectionPart.setPrice(dto.getPrice2());
        }
        if (dto.getLookups2() != null) {
            sectionPart.setLookups(dto.getLookups2());
        }
        if (dto.getClientIdentificationData2() != null) {
            sectionPart.setClientIdentificationData(dto.getClientIdentificationData2());
        }
        if (dto.getSections2() != null) {
            sectionPart.setSections(dto.getSections2());
        }
        if (dto.getNotesReferenceKeys2() != null) {
            sectionPart.setNotesReferenceKeys(dto.getNotesReferenceKeys2());
        }
        return sectionPart;
    }
}
