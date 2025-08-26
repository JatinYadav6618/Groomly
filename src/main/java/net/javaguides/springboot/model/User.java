package net.javaguides.springboot.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name =  "user", uniqueConstraints =
@UniqueConstraint(columnNames = "email"))
public class User {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;

	@Email(message = "Invalid email format", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	
	private String password;

	@Column(name="phone_number")
	private String phoneNumber;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(
		            name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				            name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "barber", cascade = CascadeType.ALL)
	private List<Schedule> schedules;

	@OneToMany(mappedBy = "barber",cascade = CascadeType.ALL)
	private List<Booking> bookingsBarber;

	@OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
	private List<Booking> bookingsClient;
	
	public User() {
		
	}
	
	public User(String firstName, String lastName, String email, String password, String phoneNumber, Set<Role> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phoneNumber=phoneNumber;
		this.roles = roles;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	public List<Booking> getBookingsBarber() {
		return bookingsBarber;
	}

	public void setBookingsBarber(List<Booking> bookingsBarber) {
		this.bookingsBarber = bookingsBarber;
	}

	public List<Booking> getBookingsClient() {
		return bookingsClient;
	}

	public void setBookingsClient(List<Booking> bookingsClient) {
		this.bookingsClient = bookingsClient;
	}

	@Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }



}
