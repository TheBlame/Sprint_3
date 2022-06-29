package org.example.pojo.getorderlist;

import org.example.pojo.getorderlist.nestedclasses.AvailableStations;
import org.example.pojo.getorderlist.nestedclasses.Orders;
import org.example.pojo.getorderlist.nestedclasses.PageInfo;

import java.util.List;

public class GetOrderListResponse {
    private List<Orders> orders;
    private PageInfo pageInfo;
    private List<AvailableStations> availableStations;

    public List<Orders> getOrders() {
        return orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<AvailableStations> getAvailableStations() {
        return availableStations;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public void setAvailableStations(List<AvailableStations> availableStations) {
        this.availableStations = availableStations;
    }
}
