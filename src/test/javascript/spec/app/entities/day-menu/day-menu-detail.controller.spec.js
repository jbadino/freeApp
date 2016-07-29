'use strict';

describe('Controller Tests', function() {

    describe('DayMenu Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDayMenu, MockWeekMenu, MockMenuItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDayMenu = jasmine.createSpy('MockDayMenu');
            MockWeekMenu = jasmine.createSpy('MockWeekMenu');
            MockMenuItem = jasmine.createSpy('MockMenuItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DayMenu': MockDayMenu,
                'WeekMenu': MockWeekMenu,
                'MenuItem': MockMenuItem
            };
            createController = function() {
                $injector.get('$controller')("DayMenuDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'freeFoodApp:dayMenuUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
