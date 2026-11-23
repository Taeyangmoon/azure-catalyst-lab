<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List,com.contoso.orders.Order" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Contoso 주문 포털</title></head>
<body>
  <h1>Contoso 주문 포털 (현대화: Azure Container Apps)</h1>
  <form method="post" action="orders">
    고객 <input name="customer" required>
    상품 <input name="item" required>
    수량 <input name="quantity" type="number" value="1" min="1" required>
    <button type="submit">주문</button>
  </form>
  <form method="post" action="upload" enctype="multipart/form-data">
    첨부 파일 <input type="file" name="file"> <button type="submit">업로드</button>
  </form>
  <table border="1" cellpadding="4">
    <tr><th>ID</th><th>고객</th><th>상품</th><th>수량</th><th>시간</th></tr>
    <% List<Order> orders = (List<Order>) request.getAttribute("orders");
       if (orders != null) for (Order o : orders) { %>
    <tr><td><%= o.getId() %></td><td><%= o.getCustomer() %></td><td><%= o.getItem() %></td><td><%= o.getQuantity() %></td><td><%= o.getCreatedAt() %></td></tr>
    <% } %>
  </table>
</body>
</html>
