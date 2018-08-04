package jd

import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.lang.System


fun main(args:Array<String>)
{

   val logger = LoggerFactory.getLogger("main")

    logger.info ("let the fun begin")

   val parser = Parser()
   val opcodes = parser.parseIt(InputStreamReader(System.`in`))
   val results = Engine.generate(opcodes)

   logger.debug("dumping opcodes: $opcodes")
   results.forEach { println(it) }

}