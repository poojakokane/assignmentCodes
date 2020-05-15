package com.example;

public class Role {

    private String location;
    private String role;
    private boolean isSpy;



    public Role(String location,String role) {
        this.location = location;
        this.role = role;
    }


    public Role() {
        isSpy = true;
    }

    public String toString() {
        if(isSpy) {
            return "This person is the spy!";
        } else
            return "This person is a " + role + " at a " + location;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isSpy ? 1231 : 1237);
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Role))
            return false;
        Role other = (Role) obj;
        if (isSpy != other.isSpy)
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        return true;
    }

}
