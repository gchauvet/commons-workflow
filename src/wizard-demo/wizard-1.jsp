<html>
<head>
<title>Workflow Wizard Demo - Page 1 of 3</title>
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
    <th align="right">First Name:</th>
    <td align="left">
      <input type="text" name="firstName" size="30"
            value="<%= bean.getFirstName() %>">
    </td>
  </tr>

  <tr>
    <th align="right">Last Name:</th>
    <td align="left">
      <input type="text" name="lastName" size="30"
            value="<%= bean.getLastName() %>">
    </td>
  </tr>

  <tr>
    <td align="center" colspan="2">
      <input type="hidden" name="step" value="unknown">
      <input type="reset" value="Reset">
      <input type="submit" value="Next" onclick="submitForm('wizard-2')">
      <input type="submit" value="Finish" onclick="submitForm('finish')">
    </td>
  </tr>

</table>

</form>

</body>
</html>
