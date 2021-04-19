package com.sun.os;

import com.sun.os.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

//        checkSelfPermission();
    }

    private void checkSelfPermission() {
        String[] permissions = new String[]{"ohos.permission.CAMERA", "ohos.permission.READ_USER_STORAGE", "ohos.permission.WRITE_USER_STORAGE"};
        requestPermissionsFromUser(permissions, 1001);
    }
}
