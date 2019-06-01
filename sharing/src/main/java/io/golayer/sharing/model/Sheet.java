package io.golayer.sharing.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sheet {
    HR_REPORT("HRReport"),
    ACTUALS("Actuals"),
    ASSUMPTIONS("Assumptions"),
    DASHBOARD("Dashboard");

    private final String name;
}
