<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List,com.contoso.orders.Order" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Contoso Order Portal</title></head>
<body>
  <h1>Contoso Order Portal (legacy)</h1>
  <form method="post" action="orders">
    Customer <input name="customer" required>
    Item <input name="item" required>
    Quantity <input name="quantity" type="number" value="1" min="1" required>
    <button type="submit">Order</button>
  </form>
  <form method="post" action="upload" enctype="multipart/form-data">
    Attachment <input type="file" name="file"> <button type="submit">Upload</button>
  </form>
  <table border="1" cellpadding="4">
    <tr><th>ID</th><th>Customer</th><th>Item</th><th>Quantity</th><th>Time</th></tr>
    <% List<Order> orders = (List<Order>) request.getAttribute("orders");
       if (orders != null) for (Order o : orders) { %>
    <tr><td><%= o.getId() %></td><td><%= o.getCustomer() %></td><td><%= o.getItem() %></td><td><%= o.getQuantity() %></td><td><%= o.getCreatedAt() %></td></tr>
    <% } %>
  </table>
</body>
</html>
