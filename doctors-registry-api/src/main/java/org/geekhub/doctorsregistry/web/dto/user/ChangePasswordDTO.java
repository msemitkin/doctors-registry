package org.geekhub.doctorsregistry.web.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChangePasswordDTO {
    @NotNull
    @NotBlank
    private String oldPassword;
    @NotNull
    @NotBlank
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
