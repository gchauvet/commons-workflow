<html>
<head>
<title>Workflow Wizard Demo - Begin</title>
</head>
<body bgcolor="white">

<jsp:useBean id="bean" scope="session"
          class="org.apache.commons.workflow.web.DemoBean"/>

<p>This web application illustrates a very simple wizard-style user interface
based upon the <em>Workflow</em> package that is currently in the sandbox of
the Jakarta Commons project.  The actual steps of the activity being
executed are in the <code>/WEB-INF/wizard.xml</code> definition file
within the web application.</p>

<p>The demo simply populates properties of a JavaBean, spread across multiple
input pages.  Navigation controls on each page of the wizard allow the user
to proceed to the previous page, the next page, or to finish the wizard (in
the usual style for this interface design pattern.</p>

<form method="POST" action="wizard">
  <input type="hidden" name="step" value="wizard-0">
  <input type="submit" value="Start">
</form>

</body>
</html>
