package gr.iag.dgtl.inventory.mapper

import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.ws.rs.core.Response
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator
import spock.lang.Specification

class ConstraintViolationExceptionMapperSpec extends Specification {

    def 'mapper transforms the constraint violation exception correctly'(){
        given: 'the validator and the mapper'
        Validator validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator()
        ConstraintViolationExceptionMapper mapper
                = new ConstraintViolationExceptionMapper()

        when: 'the mapper '
        Set<ConstraintViolation<ValidatedBean>> violations
                = validator.validate(new ValidatedBean())
        def exception = new ConstraintViolationException(
                new HashSet<ConstraintViolation<?>>(violations))
        Response res = mapper.toResponse(exception)

        then: 'a readable response'
        res.status == Response.Status.BAD_REQUEST.statusCode
        res.getEntity().errors.size() == 2
        res.getEntity().errors.contains('blank must not be blank')
        res.getEntity().errors.contains('id must not be null')
    }
}