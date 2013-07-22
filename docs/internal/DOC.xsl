<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:hwk="http://udfv.com/hwk/">

<xsl:template match="hwk:doc">
<html>
<head>
<title>UDFV Document</title>
<link rel="stylesheet" href="udfv.css" />
</head>
<body>
<h1 class="banner">UDFV Document</h1>
<div align="right">
(C) 2005 Heart Solutions, Inc.
</div>
<!--
<xsl:apply-templates select="hwk:index-doc"/>
-->
<xsl:apply-templates select="hwk:main-doc"/>
<xsl:apply-templates select="hwk:appendix-doc"/>
</body>
</html>
</xsl:template>

<xsl:template match="hwk:index-doc">
<div class="index">
<h1>目次</h1>
<xsl:apply-templates select="hwk:index"/>
</div>
</xsl:template>

<xsl:template match="hwk:index">
<ul><xsl:apply-templates select="hwk:indexitem"/></ul>
</xsl:template>

<xsl:template match="hwk:indexitem">
<li><xsl:value-of select="text()"/><br /><xsl:apply-templates select="hwk:index"/></li>
</xsl:template>

<xsl:template match="hwk:chaptor">
<div class="chaptor">
<h1><xsl:value-of select="hwk:chapno"/>. <xsl:apply-templates select="hwk:title"/></h1>
<xsl:apply-templates select="hwk:text"/>
</div>
</xsl:template>

<xsl:template match="hwk:appendix">
<div class="appendix">
<h1>付録 <xsl:value-of select="hwk:chapno"/> <xsl:apply-templates select="hwk:title"/></h1>
<xsl:apply-templates select="hwk:text"/>
</div>
</xsl:template>

<xsl:template match="hwk:section">
<div class="section">
<h2><xsl:value-of select="hwk:chapno"/>. <xsl:apply-templates select="hwk:title"/></h2>
<xsl:apply-templates select="hwk:text"/>
</div>
</xsl:template>

<xsl:template match="hwk:subsection">
<div class="section">
<h3><xsl:value-of select="hwk:chapno"/>. <xsl:apply-templates select="hwk:title"/></h3>
<xsl:apply-templates select="hwk:text"/>
</div>
</xsl:template>

<xsl:template match="hwk:subsubsection">
<div class="section">
<h4><xsl:value-of select="hwk:chapno"/>. <xsl:apply-templates select="hwk:title"/></h4>
<xsl:apply-templates select="hwk:text"/>
</div>
</xsl:template>

<xsl:template match="hwk:footnote">
<sup>†1</sup>
</xsl:template>

<xsl:template match="hwk:title">
<xsl:apply-templates select="text()|hwk:a"/>
</xsl:template>

<xsl:template match="hwk:p">
<p><xsl:apply-templates select="text()|hwk:a"/></p>
</xsl:template>

<xsl:template match="hwk:a">
<xsl:if test="@name != ''">
<a name="{@name}"><xsl:value-of select="."/></a>
</xsl:if>
<xsl:if test="@href != ''">
<a href="{@href}"><xsl:value-of select="."/></a>
</xsl:if>
</xsl:template>

<!--
<xsl:template match="text()">
<xsl:value-of select="."/>
</xsl:template>
-->

<xsl:template match="hwk:pre">
<pre><xsl:value-of select="."/></pre>
</xsl:template>

<xsl:template match="hwk:ul">
<ul><xsl:apply-templates select="hwk:li"/></ul>
</xsl:template>

<xsl:template match="hwk:ol">
<ol><xsl:apply-templates select="hwk:li"/></ol>
</xsl:template>

<xsl:template match="hwk:li">
<li><xsl:value-of select="."/></li>
</xsl:template>

</xsl:stylesheet>
