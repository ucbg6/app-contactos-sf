/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.contact;

import java.util.Map;

/**
 * Campo del objeto SFContact
 * @author ucb40
 */
public class SFField {
    private String name;
    private String sfDataType;
    private String[] valuesFromJson;
    private String oldValue;
    private String newValue;
    private boolean isCustom;
    Map<String, String> attributes;
}
