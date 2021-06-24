package org.heartfulness.avtc.model.AfterCallClasses;

import java.util.List;

public class Ld {

    private String _rst;
    private String _did;
    private List<Rr> rr;
    private String _ds;
    private List<Object> _tt;

    private String _su;
    private Integer _st;
    private Integer _et;
    private String _dr;//duration taken to pick up
    private String _ac;//Indicates call has been picked up

    public List<Rr> getRr() {
        return rr;
    }

    public void setRr(List<Rr> rr) {
        this.rr = rr;
    }

    public String get_rst() {
        return _rst;
    }

    public void set_rst(String _rst) {
        this._rst = _rst;
    }

    public String get_did() {
        return _did;
    }

    public void set_did(String _did) {
        this._did = _did;
    }

    public String get_ds() {
        return _ds;
    }

    public void set_ds(String _ds) {
        this._ds = _ds;
    }

    public List<Object> get_tt() {
        return _tt;
    }

    public void set_tt(List<Object> _tt) {
        this._tt = _tt;
    }

    public String get_su() {
        return _su;
    }

    public void set_su(String _su) {
        this._su = _su;
    }

    public Integer get_st() {
        return _st;
    }

    public void set_st(Integer _st) {
        this._st = _st;
    }

    public Integer get_et() {
        return _et;
    }

    public void set_et(Integer _et) {
        this._et = _et;
    }

    public String get_dr() {
        return _dr;
    }

    public void set_dr(String _dr) {
        this._dr = _dr;
    }

    public String get_ac() {
        return _ac;
    }

    public void set_ac(String _ac) {
        this._ac = _ac;
    }
}
