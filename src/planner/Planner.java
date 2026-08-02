/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */


/**
 *
 * @author luqman
 */
package planner;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import planner.model.Mapping;

import java.util.List;

public interface Planner {
    Mapping plan(List<Cloudlet> tasks, List<Vm> vms);
    String name();
}
