<html>
<head>
<title>Workflow Wizard Demo - End</title>
</head>
<body bgcolor="white">

<jsp:useBean id="bean" scope="session"
          class="org.apache.commons.workflow.web.DemoBean"/>

<p>The collected information about this person was:</p>

<table border="0" cellpadding="5">

  <tr>
    <th align="right">First Name:</th>
    <td align="left">
      <jsp:getProperty name="bean" property="firstName"/>
    </td>
  </tr>

  <tr>
    <th align="right">Last Name:</th>
    <td align="left">
      <jsp:getProperty name="bean" property="lastName"/>
    </td>
  </tr>

  <tr>
    <th align="right">Favorite Car:</th>
    <td align="left">
      <jsp:getProperty name="bean" property="favoriteCar"/>
    </td>
  </tr>

  <tr>
    <th align="right">Favorite City:</th>
    <td align="left">
      <jsp:getProperty name="bean" property="favoriteCity"/>
    </td>
  </tr>

  <tr>
    <th align="right">Favorite Sport:</th>
    <td align="left">
      <jsp:getProperty name="bean" property="favoriteSport"/>
    </td>
  </tr>

  <tr>
    <th align="right">Favorite Team:</th>
    <td align="left">
      <jsp:getProperty name="bean" property="favoriteTeam"/>
    </td>
  </tr>

</table>

<form method="POST" action="wizard">
  <input type="hidden" name="step" value="wizard-0">
  <input type="submit" value="Restart">
</form>

</body>
</html>
