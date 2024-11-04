package com.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.model.VinPartOptionDto;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class VinPartOption {
    private Long id;

    private String driveType;

    private String weatherType;

    private String plantCode;

    private String vinTag;

    private String options;

    private String excludedOptions;

    private String uccCodes;

    private String oemRelatedPart;

    private String oemPart;

    private String localPart;

    private String localPartSourceType;

    private String supersessionCodeUS;

    private Integer optionWeight;

    private String nations;

    private String excludedNations;

    private Integer optionCount;

    private String partColor;

    private String otherEBOM;

    private String localModelOptionCode;

    private String sectionNumberDisplay;

    private Byte isBomPart;

    private String lookups;

    private String bomPartSections;

    private Integer UID;

    private Byte alsoIllustratedInChart;

    private Byte associatedPart;

    private String baseCode;

    private String beginDate;

    private String calibrationAVSStream;

    private String calibrationCatchCode;

    private String calibrationHeader;

    private String calibrationIdentificationNumber;

    private String calibrationNumber;

    private String calibrationNumberDescription;

    private String calloutRemarkDescription;

    @Override
    public String toString() {
        return "VinPartOption{" +
                "id=" + id +
                ", driveType='" + driveType + '\'' +
                ", weatherType='" + weatherType + '\'' +
                ", plantCode='" + plantCode + '\'' +
                ", vinTag='" + vinTag + '\'' +
                ", options='" + options + '\'' +
                ", excludedOptions='" + excludedOptions + '\'' +
                ", uccCodes='" + uccCodes + '\'' +
                ", oemRelatedPart='" + oemRelatedPart + '\'' +
                ", oemPart='" + oemPart + '\'' +
                ", localPart='" + localPart + '\'' +
                ", localPartSourceType='" + localPartSourceType + '\'' +
                ", supersessionCodeUS='" + supersessionCodeUS + '\'' +
                ", optionWeight=" + optionWeight +
                ", nations='" + nations + '\'' +
                ", excludedNations='" + excludedNations + '\'' +
                ", optionCount=" + optionCount +
                ", partColor='" + partColor + '\'' +
                ", otherEBOM='" + otherEBOM + '\'' +
                ", localModelOptionCode='" + localModelOptionCode + '\'' +
                ", sectionNumberDisplay='" + sectionNumberDisplay + '\'' +
                ", isBomPart=" + isBomPart +
                ", lookups='" + lookups + '\'' +
                ", bomPartSections='" + bomPartSections + '\'' +
                ", UID=" + UID +
                ", alsoIllustratedInChart=" + alsoIllustratedInChart +
                ", associatedPart=" + associatedPart +
                ", baseCode='" + baseCode + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", calibrationAVSStream='" + calibrationAVSStream + '\'' +
                ", calibrationCatchCode='" + calibrationCatchCode + '\'' +
                ", calibrationHeader='" + calibrationHeader + '\'' +
                ", calibrationIdentificationNumber='" + calibrationIdentificationNumber + '\'' +
                ", calibrationNumber='" + calibrationNumber + '\'' +
                ", calibrationNumberDescription='" + calibrationNumberDescription + '\'' +
                ", calloutRemarkDescription='" + calloutRemarkDescription + '\'' +
                ", decode='" + decode + '\'' +
                ", effectiveDateDescription='" + effectiveDateDescription + '\'' +
                ", endDate='" + endDate + '\'' +
                ", exportMarketPart=" + exportMarketPart +
                ", finisCode='" + finisCode + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupKey='" + groupKey + '\'' +
                ", groupBasicBeginDate='" + groupBasicBeginDate + '\'' +
                ", groupBasicEndDate='" + groupBasicEndDate + '\'' +
                ", handType='" + handType + '\'' +
                ", hasImage=" + hasImage +
                ", hasKit=" + hasKit +
                ", hasNote=" + hasNote +
                ", hasPartsList=" + hasPartsList +
                ", illustratedInChart=" + illustratedInChart +
                ", interpretationCodes='" + interpretationCodes + '\'' +
                ", isExportMarketSection=" + isExportMarketSection +
                ", isGroup=" + isGroup +
                ", lightTruckDescription='" + lightTruckDescription + '\'' +
                ", mainPart=" + mainPart +
                ", modelCode='" + modelCode + '\'' +
                ", motorCraftNumber='" + motorCraftNumber + '\'' +
                ", searchPartId='" + searchPartId + '\'' +
                ", partId='" + partId + '\'' +
                ", partListId='" + partListId + '\'' +
                ", partListKey='" + partListKey + '\'' +
                ", partListNumber='" + partListNumber + '\'' +
                ", partNameCode='" + partNameCode + '\'' +
                ", partTypeCode='" + partTypeCode + '\'' +
                ", recallNumber='" + recallNumber + '\'' +
                ", refersToGroup=" + refersToGroup +
                ", refersToPartsList=" + refersToPartsList +
                ", remark='" + remark + '\'' +
                ", replacingPartNumber='" + replacingPartNumber + '\'' +
                ", restrictionDescription='" + restrictionDescription + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", sectionPath='" + sectionPath + '\'' +
                ", source='" + source + '\'' +
                ", usageDescription='" + usageDescription + '\'' +
                ", type='" + type + '\'' +
                ", notAvailable='" + notAvailable + '\'' +
                ", treatAsNineties=" + treatAsNineties +
                ", identificationNumbers='" + identificationNumbers + '\'' +
                ", componentId=" + componentId +
                ", componentSourceId=" + componentSourceId +
                ", notes='" + notes + '\'' +
                ", sections='" + sections + '\'' +
                ", calloutKeys='" + calloutKeys + '\'' +
                ", localProduct=" + localProduct +
                ", markets='" + markets + '\'' +
                ", sectionApplicability=" + sectionApplicability +
                ", alternatePartList='" + alternatePartList + '\'' +
                ", catalogCode='" + catalogCode + '\'' +
                ", partCode='" + partCode + '\'' +
                ", description='" + description + '\'' +
                ", quantity='" + quantity + '\'' +
                ", supersessionCode='" + supersessionCode + '\'' +
                ", supersessionCodeDescription='" + supersessionCodeDescription + '\'' +
                ", supersessionAbbreviation='" + supersessionAbbreviation + '\'' +
                ", supersessionAbbreviationDescription='" + supersessionAbbreviationDescription + '\'' +
                ", supersessionAbbreviationLexicon='" + supersessionAbbreviationLexicon + '\'' +
                ", hashcode=" + hashcode +
                ", automakerDescription='" + automakerDescription + '\'' +
                ", includeCountries='" + includeCountries + '\'' +
                ", excludeCountries='" + excludeCountries + '\'' +
                ", applicable=" + applicable +
                ", applicable2=" + applicable2 +
                ", excluded=" + excluded +
                '}';
    }

    private String decode;

    private String effectiveDateDescription;

    private String endDate;

    private Byte exportMarketPart;

    private String finisCode;

    private String groupDescription;

    private String groupKey;

    private String groupBasicBeginDate;

    private String groupBasicEndDate;

    private String handType;

    private Byte hasImage;

    private Byte hasKit;

    private Byte hasNote;

    private Byte hasPartsList;

    private Byte illustratedInChart;

    private String interpretationCodes;

    private Byte isExportMarketSection;

    private Byte isGroup;

    private String lightTruckDescription;

    private Byte mainPart;

    private String modelCode;

    private String motorCraftNumber;

    private String searchPartId;

    private String partId;

    private String partListId;

    private String partListKey;

    private String partListNumber;

    private String partNameCode;

    private String partTypeCode;

    private String recallNumber;

    private Byte refersToGroup;

    private Byte refersToPartsList;

    private String remark;

    private String replacingPartNumber;

    private String restrictionDescription;

    private String sectionId;

    private String sectionPath;

    private String source;

    private String usageDescription;

    private String type;

    private String notAvailable;

    private Byte treatAsNineties;

    private String identificationNumbers;

    private Integer componentId;

    private Integer componentSourceId;

    private String notes;

    private String sections;

    private String calloutKeys;

    private Byte localProduct;

    private String markets;

    private Byte sectionApplicability;

    private String alternatePartList;

    private String catalogCode;

    private String partCode;

    private String description;

    private String quantity;

    private String supersessionCode;

    private String supersessionCodeDescription;

    private String supersessionAbbreviation;

    private String supersessionAbbreviationDescription;

    private String supersessionAbbreviationLexicon;

    private Integer hashcode;

    private String automakerDescription;

    private String includeCountries;

    private String excludeCountries;

    private Byte applicable;

    private Boolean applicable2 = true;

    private Byte excluded;

    public static VinPartOption convert(VinPartOptionDto dto) {
        VinPartOption option = new VinPartOption();
        BeanUtils.copyProperties(dto, option);
        option.setOptions(dto.getOptions().toJSONString(2));
        option.setExcludedOptions(dto.getExcludedOptions().toJSONString(2));
        option.setUccCodes(dto.getUccCodes().toJSONString(2));
        option.setNations(dto.getNations().toJSONString(2));
        option.setExcludedNations(dto.getExcludedNations().toJSONString(2));
        option.setLookups(dto.getLookups().toJSONString(2));
        option.setBomPartSections(dto.getBomPartSections().toJSONString(2));
        option.setIdentificationNumbers(dto.getIdentificationNumbers().toJSONString(2));
        option.setNotes(dto.getNotes().toJSONString(2));
        option.setSections(dto.getSections().toJSONString(2));
        option.setCalloutKeys(dto.getCalloutKeys().toJSONString(2));
        option.setMarkets(dto.getMarkets().toJSONString(2));
        option.setAlternatePartList(dto.getAlternatePartList().toJSONString(2));
        option.setPartColor(dto.getPartColor().toJSONString(2));
        if (dto.getIncludeCountries() != null) {
            option.setIncludeCountries(dto.getIncludeCountries().toJSONString(2));
        }
        if (dto.getExcludeCountries() != null) {
            option.setExcludeCountries(dto.getExcludeCountries().toJSONString(2));
        }
        return option;
    }

}