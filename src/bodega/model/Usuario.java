/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;

/**
 *
 * @author SEGUIRESA-PC
 */
public class Usuario {
    private String username;
    private String password;
    private String[] privilegios;

    public Usuario(String username, String password, String[] privilegios) {
        this.username = username;
        this.password = password;
        this.privilegios = privilegios;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getPrivilegios() {
        return privilegios;
    }

    public void setPrivilegios(String[] privilegios) {
        this.privilegios = privilegios;
    }
    
}
