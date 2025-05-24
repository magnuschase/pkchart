package org.magnuschase.pkchart.model;

import lombok.Getter;

@Getter
public enum CardCondition {
    // PSA Grades
    PSA_10("PSA 10"),
    PSA_9("PSA 9"),
    PSA_8("PSA 8"),
    PSA_7("PSA 7"),
    PSA_6("PSA 6"),
    PSA_5("PSA 5"),
    PSA_4("PSA 4"),
    PSA_3("PSA 3"),
    PSA_2("PSA 2"),
    PSA_1_5("PSA 1.5"),
    PSA_1("PSA 1"),

    // CGC Grades
    CGC_10_PRISTINE("CGC 10 Pristine"),
    CGC_10_GEM_MINT("CGC 10 Gem Mint"),
    CGC_9_5("CGC 9.5"),
    CGC_9("CGC 9"),
    CGC_8_5("CGC 8.5"),
    CGC_8("CGC 8"),
    CGC_7_5("CGC 7.5"),
    CGC_7("CGC 7"),
    CGC_6_5("CGC 6.5"),
    CGC_6("CGC 6"),
    CGC_5_5("CGC 5.5"),
    CGC_5("CGC 5"),
    CGC_4_5("CGC 4.5"),
    CGC_4("CGC 4"),
    CGC_3_5("CGC 3.5"),
    CGC_3("CGC 3"),
    CGC_2_5("CGC 2.5"),
    CGC_2("CGC 2"),
    CGC_1_5("CGC 1.5"),
    CGC_1("CGC 1"),

    // BGS Grades
    BGS_10_BLACK_LABEL("BGS 10 Black Label"),
    BGS_10("BGS 10"),
    BGS_9_5("BGS 9.5"),
    BGS_9("BGS 9"),
    BGS_8_5("BGS 8.5"),
    BGS_8("BGS 8"),
    BGS_7_5("BGS 7.5"),
    BGS_7("BGS 7"),
    BGS_6_5("BGS 6.5"),
    BGS_6("BGS 6"),
    BGS_5_5("BGS 5.5"),
    BGS_5("BGS 5"),
    BGS_4_5("BGS 4.5"),
    BGS_4("BGS 4"),
    BGS_3_5("BGS 3.5"),
    BGS_3("BGS 3"),
    BGS_2_5("BGS 2.5"),
    BGS_2("BGS 2"),
    BGS_1_5("BGS 1.5"),
    BGS_1("BGS 1"),

    // TAG Grades
    TAG_10("TAG 10"),
    TAG_9_5("TAG 9.5"),
    TAG_9("TAG 9"),
    TAG_8_5("TAG 8.5"),
    TAG_8("TAG 8"),
    TAG_7_5("TAG 7.5"),
    TAG_7("TAG 7"),
    TAG_6_5("TAG 6.5"),
    TAG_6("TAG 6"),
    TAG_5_5("TAG 5.5"),
    TAG_5("TAG 5"),
    TAG_4_5("TAG 4.5"),
    TAG_4("TAG 4"),
    TAG_3_5("TAG 3.5"),
    TAG_3("TAG 3"),
    TAG_2_5("TAG 2.5"),
    TAG_2("TAG 2"),
    TAG_1_5("TAG 1.5"),
    TAG_1("TAG 1"),

    // ARS Grades
    ARS_10("ARS 10"),
    ARS_9_5("ARS 9.5"),
    ARS_9("ARS 9"),
    ARS_8_5("ARS 8.5"),
    ARS_8("ARS 8"),
    ARS_7_5("ARS 7.5"),
    ARS_7("ARS 7"),
    ARS_6_5("ARS 6.5"),
    ARS_6("ARS 6"),
    ARS_5_5("ARS 5.5"),
    ARS_5("ARS 5"),
    ARS_4_5("ARS 4.5"),
    ARS_4("ARS 4"),
    ARS_3_5("ARS 3.5"),
    ARS_3("ARS 3"),
    ARS_2_5("ARS 2.5"),
    ARS_2("ARS 2"),
    ARS_1_5("ARS 1.5"),
    ARS_1("ARS 1"),

    // Raw (ungraded) card conditions from Cardmarket
    RAW_MINT("MT - Mint (Raw)"),
    RAW_NEAR_MINT("NM - Near Mint (Raw)"),
    RAW_EXCELLENT("EX - Excellent (Raw)"),
    RAW_GOOD("GD - Good (Raw)"),
    RAW_LIGHT_PLAYED("LP - Light Played (Raw)"),
    RAW_PLAYED("PL - Played (Raw)"),
    RAW_POOR("PO - Poor (Raw)");

    private final String displayName;

    CardCondition(String displayName) {
        this.displayName = displayName;
    }
}
