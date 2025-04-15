package com.hexaware.petpals.entity;

public class Pet {
    private String name;
    private int age;
    private String breed;
    private String type;

    public Pet(String name, int age, String breed) {
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.type = "unknown";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }
    
    public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void adopt() {
        System.out.println(name + " has been adopted!");
    }

    @Override
    public String toString() {
        return "Pet [Name=" + name + ", Age=" + age + ", Breed=" + breed + "]";
    }
}
