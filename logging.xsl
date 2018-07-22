<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match='log'>
        <html>
            <style>
                table
                {
                    border-collapse: collapse;
                }
                th
                {
                    background-color: snow;
                }
                td, th
                {
                    border: 1px solid black;
                    padding: 15px;
                }
            </style>
            <body>
                <table>
                    <tr>
                        <th>Date</th>
                        <th>Millis</th>
                        <th>Nanos</th>
                        <th>Sequence</th>
                        <th>Logger</th>
                        <th>Level</th>
                        <th>Class</th>
                        <th>Method</th>
                        <th>Thread</th>
                        <th>Message</th>
                    </tr>
                    <xsl:apply-templates/>
                </table>
            </body>
        </html>
    </xsl:template>
    <xsl:template match='record'>
        <tr><xsl:apply-templates/></tr>
    </xsl:template>

    <xsl:template match='date'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='millis'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='nanos'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='sequence'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='logger'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='level'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='level[text()="SEVERE"]'>
        <td style="color: red"><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='level[text()="WARNING"]'>
        <td style="color: orange"><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='level[text()="INFO"]'>
        <td style="color: green"><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='class'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='method'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='thread'>
        <td><xsl:apply-templates/></td>
    </xsl:template>

    <xsl:template match='message'>
        <td><xsl:apply-templates/></td>
    </xsl:template>
</xsl:stylesheet>