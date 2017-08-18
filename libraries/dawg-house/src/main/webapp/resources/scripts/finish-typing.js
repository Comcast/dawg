(function( $ ) {

    $.fn.finishTyping = function(callback, settings) {

        var typingTimer = null;
        settings = settings ? settings : {};
        var interval = settings.interval ? settings.interval : 1000;

        this.on('input',function(e){
          clearTimeout(typingTimer);
          typingTimer = setTimeout(callback, interval);
        });

        return this;

    };

}( jQuery ));