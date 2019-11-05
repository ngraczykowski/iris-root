package com.silenteight.sens.webapp.kernel.security;

public enum SensPermission {

    /* Decision Tree permissions */
    DECISION_TREE_VIEW(1 << 0, 'V'), // 1
    DECISION_TREE_CHANGE(1 << 1, 'C'), // 2

    /* Workflow Level permissions */
    WORKFLOW_LEVEL_ACCEPT(1 << 2, 'A'); // 4

    private final int mask;
    private final char code;

    SensPermission(int mask, char code) {
        this.mask = mask;
        this.code = code;
    }

    public int getMask() {
        return mask;
    }

    public char getCode() {
        return code;
    }
}

