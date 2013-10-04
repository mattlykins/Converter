package com.mattlykins.converter;

public class Convs
{
	private Integer id;
    private String From,To,Special;
    private Double Multi,Offset;
    
    public Convs(Integer id, String from, String to, Double multi, Double offset, String special) {
        super();
        this.setId(id);
        setFrom(from);
        setTo(to);
        setSpecial(special);
        setMulti(multi);
        setOffset(offset);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getSpecial() {
        return Special;
    }

    public void setSpecial(String special) {
        Special = special;
    }

    public Double getMulti() {
        return Multi;
    }

    public void setMulti(Double multi) {
        Multi = multi;
    }

    public Double getOffset() {
        return Offset;
    }

    public void setOffset(Double offset) {
        Offset = offset;
    }
    }
