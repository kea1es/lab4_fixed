package beansLab.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "shots")
public class Shot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shot_id")
    private long id;

    @Column(name = "x")
    private double x;

    @Column(name = "y")
    private double y;

    @Column(name = "r")
    private double r;

    @Column(name = "rg")
    private boolean rg;

    @Column(name = "start_time")
    private String start;

    @Column(name = "script_time")
    private long scriptTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setX(double x) { // ИЗМЕНЕНО: float → double
        this.x = x;
    }

    public void setY(double y) { // ИЗМЕНЕНО: float → double
        this.y = y;
    }

    public void setR(double r) { // ИЗМЕНЕНО: float → double
        this.r = r;
    }

    public void setRG(boolean rg) {
        this.rg = rg;
    }

    public void setStart(LocalDateTime start) {
        String str = start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy;HH:mm:ss"));
        this.start = str;
    }

    public void setScriptTime(long scriptTime) {
        this.scriptTime = scriptTime;
    }

    public double getX() { // ИЗМЕНЕНО: float → double
        return x;
    }

    public double getY() { // ИЗМЕНЕНО: float → double
        return y;
    }

    public double getR() { // ИЗМЕНЕНО: float → double
        return r;
    }

    public boolean isRG() {
        return rg;
    }

    public LocalDateTime getStart() {
        return LocalDateTime.parse(start, DateTimeFormatter.ofPattern("dd-MM-yyyy;HH:mm:ss"));
    }

    public long getScriptTime() {
        return scriptTime;
    }

    public Shot() {}

    @Override
    public String toString() {
        return "Shot:" + id + " " +
                "X: " + x + " " +
                "Y: " + y + " " +
                "R: " + r + " " +
                "GR: " + rg + " " +
                "Start time: " + start + " " +
                "Script time: " + scriptTime;
    }
}