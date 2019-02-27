package schedulingApp.resources;

import java.util.ListResourceBundle;

public class Scheduling_ge_DE extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
                {"intro", "Standardgebietsschema ausgewählt"},
                {"openConnection", "Verbindung offen"},
                {"userName", "Benutzername"},
                {"password", "Passwort"},
                {"signIn", "Anmelden"},
                {"match", "Benutzername / Passwort stimmen überein!"},
                {"signInButton", "Anmelden"},
                {"schedulingApp", "die Planungs-App"},
                {"loginSuccess", "Erfolgreich angemeldet..."},
                {"loginSuccessDescription", "Benutzername und Passwort stimmen überein."},
                {"invalidCredentials", "Ungültige Anmeldeinformationen"}
        };
    }
}
