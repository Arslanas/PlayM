package my.app.playm.aop;

import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Data;
import my.app.playm.controller.Dispatcher;
import my.app.playm.model.moment.MomentRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j
public class PlayMAspect {

    @Autowired
    MomentRepository momentRepository;
    @Autowired
    Dispatcher dispatcher;

    @Around("@annotation(SaveMoment)")
    public Object saveMoment(ProceedingJoinPoint joinPoint) throws Throwable {
        if (Data.isDecodeComplete) momentRepository.saveState();

        Object methodReturnValue = joinPoint.proceed();

        if (Data.isDecodeComplete) dispatcher.updateAll();
        return methodReturnValue;
    }

    @Around("@annotation(UpdateAfter)")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        Object methodReturnValue = joinPoint.proceed();

        dispatcher.updateAll();
        return methodReturnValue;
    }

}
