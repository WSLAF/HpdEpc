package org.example.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
//@EqualsAndHashCode
@ToString
public class SectionPartDto {
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

    private JSONObject userNote;

    private String userNote2;

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

//    @TableField(typeHandler = JacksonTypeHandler.class)
//    private String clientGlossaries;
    private JSONArray notes;

    private String notes2;

    private JSONObject price;

    private String price2;

    private JSONArray lookups;

    private String lookups2;

    private String fitmentInstructions;

    private JSONObject clientIdentificationData;

    private String clientIdentificationData2;

    private JSONArray sections;

    private String sections2;

    private String basePart;

    private JSONArray notesReferenceKeys;

    private String notesReferenceKeys2;

    private Integer jwfPartMasterNextPartNumber;

    private Boolean openPartPrice;

    private String applicability;

    private Boolean isOpenPartPrice;

//    @EqualsAndHashCode.Exclude
    private Long id;

//    @EqualsAndHashCode.Exclude
    private Long optionId;

    @Override
    public String toString() {
        return "SectionPartDto{" +
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
                ", userNote=" + userNote +
                ", userNote2='" + userNote2 + '\'' +
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
                ", notes=" + notes +
                ", notes2='" + notes2 + '\'' +
                ", price=" + price +
                ", price2='" + price2 + '\'' +
                ", lookups=" + lookups +
                ", lookups2='" + lookups2 + '\'' +
                ", fitmentInstructions='" + fitmentInstructions + '\'' +
                ", clientIdentificationData=" + clientIdentificationData +
                ", clientIdentificationData2='" + clientIdentificationData2 + '\'' +
                ", sections=" + sections +
                ", sections2='" + sections2 + '\'' +
                ", basePart='" + basePart + '\'' +
                ", notesReferenceKeys=" + notesReferenceKeys +
                ", notesReferenceKeys2='" + notesReferenceKeys2 + '\'' +
                ", jwfPartMasterNextPartNumber=" + jwfPartMasterNextPartNumber +
                ", openPartPrice=" + openPartPrice +
                ", applicability='" + applicability + '\'' +
                ", isOpenPartPrice=" + isOpenPartPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionPartDto that = (SectionPartDto) o;
        //return Objects.equals(catalogCode, that.catalogCode) && Objects.equals(sectionCode, that.sectionCode) && Objects.equals(applicable, that.applicable) && Objects.equals(beginDate, that.beginDate) && Objects.equals(callout, that.callout) && Objects.equals(data, that.data) && Objects.equals(endDate, that.endDate) && Objects.equals(hasImage, that.hasImage) && Objects.equals(label, that.label) && Objects.equals(automakerLabel, that.automakerLabel) && Objects.equals(qty, that.qty) && Objects.equals(remark, that.remark) && Objects.equals(type, that.type) && Objects.equals(sourceType, that.sourceType) && Objects.equals(hasNotes, that.hasNotes) && Objects.equals(userNote, that.userNote) && Objects.equals(userNote2, that.userNote2) && Objects.equals(pdfLinks, that.pdfLinks) && Objects.equals(hasTSB, that.hasTSB) && Objects.equals(hasAccNote, that.hasAccNote) && Objects.equals(hasPANT, that.hasPANT) && Objects.equals(isTranslatePart, that.isTranslatePart) && Objects.equals(hasHistoricalParts, that.hasHistoricalParts) && Objects.equals(selectiveFit, that.selectiveFit) && Objects.equals(partSequenceNo, that.partSequenceNo) && Objects.equals(brand, that.brand) && Objects.equals(selectiveFitGroupId, that.selectiveFitGroupId) && Objects.equals(avsCodes, that.avsCodes) && Objects.equals(modelCodes, that.modelCodes) && Objects.equals(revisionRef, that.revisionRef) && Objects.equals(labelLanguage, that.labelLanguage) && Objects.equals(notes, that.notes) && Objects.equals(notes2, that.notes2) && Objects.equals(price, that.price) && Objects.equals(price2, that.price2) && Objects.equals(lookups, that.lookups) && Objects.equals(lookups2, that.lookups2) && Objects.equals(fitmentInstructions, that.fitmentInstructions) && Objects.equals(clientIdentificationData, that.clientIdentificationData) && Objects.equals(clientIdentificationData2, that.clientIdentificationData2) && Objects.equals(sections, that.sections) && Objects.equals(sections2, that.sections2) && Objects.equals(basePart, that.basePart) && Objects.equals(notesReferenceKeys, that.notesReferenceKeys) && Objects.equals(notesReferenceKeys2, that.notesReferenceKeys2) && Objects.equals(jwfPartMasterNextPartNumber, that.jwfPartMasterNextPartNumber) && Objects.equals(openPartPrice, that.openPartPrice) && Objects.equals(applicability, that.applicability) && Objects.equals(isOpenPartPrice, that.isOpenPartPrice);
        return Objects.equals(catalogCode, that.catalogCode) && Objects.equals(sectionCode, that.sectionCode);
    }

    @Override
    public int hashCode() {
        //不一样，这边有usernote2
        return Objects.hash(catalogCode, sectionCode, applicable, beginDate,
                callout, data, endDate, hasImage, label, automakerLabel,
                qty, remark, type, sourceType, hasNotes, userNote2,
                pdfLinks, hasTSB, hasAccNote, hasPANT, isTranslatePart,
                hasHistoricalParts, selectiveFit, partSequenceNo, brand,
                selectiveFitGroupId, avsCodes, modelCodes, revisionRef,
                labelLanguage, notes2, price2, lookups2, fitmentInstructions,
                clientIdentificationData2, sections2, basePart,
                notesReferenceKeys2, jwfPartMasterNextPartNumber, openPartPrice,
                applicability, isOpenPartPrice);
    }
}