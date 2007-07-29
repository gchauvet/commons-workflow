<html>
<head>
<title>Workflow Wizard Demo - Page 3 of 3</title>
<script language="javascript">
  function submitForm(s) {
    document.wizardform.step.value = s;
    document.wizardform.submit();
    return false;
  }
</script>
</head>
<body bgcolor="white">

<jsp:useBean id="bean" scope="session"
          class="org.apache.commons.workflow.web.DemoBean"/>

<form name="wizardform" method="POST" action="wizard">

<table border="0" cellspacing="5">

  <tr>
    <th align="right">Favorite Sport:</th>
    <td align="left">
      <input type="text" name="favoriteSport" size="30"
            value="<%= bean.getFavoriteSport() %>">
    </td>
  </tr>

  <tr>
    <th align="right">Favorite Team:</th>
    <td align="left">
      <input type="text" name="favoriteTeam" size="30"
            value="<%= bean.getFavoriteTeam() %>">
    </td>
  </tr>

  <tr>
    <td align="center" colspan="2">
      <input type="hidden" name="step" value="unknown">
      <input type="submit" value="Previous" onclick="submitForm('wizard-2')">
      <input type="reset" value="Reset">
      <input type="submit" value="Finish" onclick="submitForm('finish')">
    </td>
  </tr>

</table>

</body>
</html>
