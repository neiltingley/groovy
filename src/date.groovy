import java.text.SimpleDateFormat

/**
 * Created by ntingley on 25/02/14.
 */

//2014-02-25_15-47-24



def inDateParser =  new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
def Date d = inDateParser.parse("2014-02-25_15-47-24")
def outDateParser=new SimpleDateFormat("yyyy-MM-dd")
println outDateParser.format(d)
