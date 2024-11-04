package org.example.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@EqualsAndHashCode
public class SectionPart {
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

    private Long optionId;

    public static SectionPart convert(SectionPartDto dto) {
        SectionPart sectionPart = new SectionPart();
        BeanUtils.copyProperties(dto, sectionPart);
        sectionPart.setUserNote(dto.getUserNote().toJSONString(2));
        sectionPart.setNotes(dto.getNotes().toJSONString(2));
        sectionPart.setPrice(dto.getPrice().toJSONString(2));
        sectionPart.setLookups(dto.getLookups().toJSONString(2));
        sectionPart.setClientIdentificationData(dto.getClientIdentificationData().toJSONString(2));
        sectionPart.setSections(dto.getSections().toJSONString(2));
        sectionPart.setNotesReferenceKeys(dto.getNotesReferenceKeys().toJSONString(2));
        sectionPart.setOptionId(dto.getOptionId());
        return sectionPart;
    }

    @Override
    public String toString() {
        return "SectionPart{" +
                "catalogCode='" + catalogCode + '\'' +
                ", sectionCode='" + sectionCode + '\'' +
                ", applicable='" + applicable + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", callout='" + callout + '\'' +
                ", data='" + data + '\'' +
                ", endDate='" + endDate + '\'' +
                ", hasImage='" + hasImage + '\'' +
                ", label='" + label + '\'' +
                ", automakerLabel='" + automakerLabel + '\'' +
                ", qty='" + qty + '\'' +
                ", remark='" + remark + '\'' +
                ", type='" + type + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", hasNotes=" + hasNotes +
                ", userNote='" + userNote + '\'' +
                ", pdfLinks='" + pdfLinks + '\'' +
                ", hasTSB=" + hasTSB +
                ", hasAccNote=" + hasAccNote +
                ", hasPANT=" + hasPANT +
                ", isTranslatePart=" + isTranslatePart +
                ", hasHistoricalParts=" + hasHistoricalParts +
                ", selectiveFit=" + selectiveFit +
                ", partSequenceNo='" + partSequenceNo + '\'' +
                ", brand='" + brand + '\'' +
                ", selectiveFitGroupId=" + selectiveFitGroupId +
                ", avsCodes='" + avsCodes + '\'' +
                ", modelCodes='" + modelCodes + '\'' +
                ", revisionRef='" + revisionRef + '\'' +
                ", labelLanguage='" + labelLanguage + '\'' +
                ", notes='" + notes + '\'' +
                ", price='" + price + '\'' +
                ", lookups='" + lookups + '\'' +
                ", fitmentInstructions='" + fitmentInstructions + '\'' +
                ", clientIdentificationData='" + clientIdentificationData + '\'' +
                ", sections='" + sections + '\'' +
                ", basePart='" + basePart + '\'' +
                ", notesReferenceKeys='" + notesReferenceKeys + '\'' +
                ", jwfPartMasterNextPartNumber=" + jwfPartMasterNextPartNumber +
                ", openPartPrice=" + openPartPrice +
                ", applicability='" + applicability + '\'' +
                '}';
    }
}
