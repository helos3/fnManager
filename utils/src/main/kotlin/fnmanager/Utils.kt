package fnmanager

/**
 * Created by rushan on 2/22/2017.
 */
//fun <T> Transaction.eval(transactionBody: () -> T) {
//
//    logger.addLogger(StdOutSqlLogger())
//    com.fnmanager.domain.domain.Exceptional.of { transactionBody.invoke() }
//            .on(false, { rollback() })
//            .on(true, { commit() })
//            .peek { close() }
//            .orElseThrow()
//}