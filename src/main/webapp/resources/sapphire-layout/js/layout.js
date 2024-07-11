/** 
 * PrimeFaces Sapphire Layout
 */
PrimeFaces.widget.Sapphire = PrimeFaces.widget.BaseWidget.extend({
    
    init: function(cfg) {
        this._super(cfg);
        this.wrapper = $(document.body).children('.layout-container');
        this.layoutMenuContainer = $('#layout-menu-container');
        this.menu = this.jq;
        this.menulinks = this.menu.find('a');
        this.menuButton = $('#layout-menubutton');
        this.profileMenuButton = $('#profile-menu-button');
        this.profileMenu = $('#layout-profile-menu');
        this.configButton = $('#layout-config-button');
        this.configMenu = $('#layout-config');
        this.configMenuClose = this.configMenu.find('> .layout-config-content > .layout-config-close');
        
        this.bindEvents();

        if (!this.isHorizontal()) {
            this.restoreMenuState();
        }
        
        this.expandedMenuitems = this.expandedMenuitems||[];
    },
    
    bindEvents: function() {
        var $this = this;

        this.layoutMenuContainer.off('click.menu').on('click.menu', function() {
            $this.menuClick = true;
        });

        this.menuButton.off('click.menu').on('click.menu', function(e) {
            $this.menuClick = true;
            $this.wrapper.toggleClass('layout-menu-active');
            $this.menuButton.toggleClass('layout-menubutton-active');

            $(document.body).toggleClass('blocked-scroll');

            e.preventDefault();
        });

        this.menulinks.off('click.menu').on('click.menu', function(e) {
            var link = $(this),
            item = link.parent(),
            submenu = item.children('div');
            horizontal = $this.isHorizontal();
            
            if (horizontal) {
                $this.horizontalMenuClick = true;
            }
            
            if (item.hasClass('active-menuitem')) {
                if (submenu.length) {
                    $this.removeMenuitem(item.attr('id'));
                    item.removeClass('active-menuitem');
                    
                    if (horizontal) {
                        if(item.parent().is($this.jq)) {
                            $this.menuActive = false;
                        }
                        
                        submenu.hide();
                    }
                    else {
                        submenu.slideUp();
                    }
                    
                    submenu.find('.ink').remove();
                }
            }
            else {
                $this.addMenuitem(item.attr('id'));

                if (horizontal) {
                    $this.deactivateItems(item.siblings());
                    $this.activate(item);
                    $this.menuActive = true;
                }
                else {
                    $this.deactivateItems(item.siblings(), true);
                    $this.activate(item);
                }
            }
            
            if (submenu.length) {
                e.preventDefault();
            }
            else {
                if ($this.isHorizontal())
                    $this.resetHorizontalMenu();
                else
                    $this.hideVerticalMenu();
            }
        });

        this.menu.find('> li').off('mouseenter.menu').on('mouseenter.menu', function(e) {
            if($this.isHorizontal()) {
                var item = $(this);
                
                if(!item.hasClass('active-menuitem')) {
                    var activeItem = item.siblings('.active-menuitem');
                    activeItem.removeClass('active-menuitem');
                    if (activeItem.find('> .layout-submenu-container > .layout-submenu').hasClass('layout-megamenu')) {
                        activeItem.children('.layout-submenu-container').hide();
                    }
                    else {
                        activeItem.find('.layout-submenu-container:visible').hide();
                    }
                    $this.menu.find('.ink').remove();
                    
                    if($this.menuActive) {
                        item.addClass('active-menuitem');
                        item.children('.layout-submenu-container').show();
                    }
                }
            }
        });

        this.profileMenuButton.off('click.profile').on('click.profile', function(e) {
            $this.profileMenuClick = true;
            $this.profileMenu.removeClass('fadeInDown fadeOutUp');
            $this.profileMenu.find('.ink').remove();

            if($this.profileMenu.hasClass('layout-profile-menu-active'))
                $this.hideProfileMenu();
            else
                $this.profileMenu.addClass('layout-profile-menu-active fadeInDown');

            e.preventDefault();
        });

        this.profileMenu.off('click.profile').on('click.profile', function() {
            $this.profileMenuClick = true;
        });

        this.profileMenu.find('a').off('click.profile').on('click.profile', function() {
            $this.hideProfileMenu();
        });
         
        this.configButton.off('click.config').on('click.config', function(e) {
            $this.configMenu.find('.ink').remove();
            $this.configMenu.removeClass('layout-config-exit-done').addClass('layout-config-enter');
            setTimeout(function() {
                $this.configMenu.addClass('layout-config-enter-active');
            }, 1);

            setTimeout(function() {
                $this.configMenu.removeClass('layout-config-enter layout-config-enter-active').addClass('layout-config-enter-done')
            }, 150);

            $(document.body).addClass('blocked-scroll-config').append('<div class="layout-config-mask"></div>');
        
            e.preventDefault();
        });

        this.configMenuClose.off('click.config').on('click.config', function(e) {
            $this.configMenu.removeClass('layout-config-enter-done').addClass('layout-config-exit');
            setTimeout(function() {
                $this.configMenu.addClass('layout-config-exit-active');
            }, 1);

            setTimeout(function() {
                $this.configMenu.removeClass('layout-config-exit layout-config-exit-active').addClass('layout-config-exit-done')
            }, 150);

            $(document.body).removeClass('blocked-scroll-config').children('.layout-config-mask').remove();
        
            e.preventDefault();
        });
        
        $(document.body).off('click.layoutBody').on('click.layoutBody', function() {
            if ($this.isHorizontal() && !$this.horizontalMenuClick) {
                $this.resetHorizontalMenu();
            }

            if (!$this.profileMenuClick) {
                $this.hideProfileMenu();
            }

            if (!$this.menuClick) {
                $this.hideVerticalMenu();
            }
                    
            $this.menuClick = false;
            $this.horizontalMenuClick = false;
            $this.profileMenuClick = false;
        });
    },

    resetHorizontalMenu: function() {
        this.menu.find('.active-menuitem').removeClass('active-menuitem');
        this.menu.find('.layout-submenu-container:not(.layout-submenu-megamenu-container):visible').hide();
        this.menuActive = false;
    },

    hideVerticalMenu: function() {
        this.wrapper.removeClass('layout-menu-active');
        this.menuButton.removeClass('layout-menubutton-active');
        $(document.body).removeClass('blocked-scroll');
    },

    hideProfileMenu: function() {
        var $this = this;
        this.profileMenu.addClass('fadeOutUp').removeClass('fadeInDown');
                
        setTimeout(function() {
            $this.profileMenu.removeClass('layout-profile-menu-active fadeOutUp');
        }, 150);
    },
        
    activate: function(item) {
        var submenu =  item.children('div');
        item.addClass('active-menuitem');

        if (submenu.length) {
            if (this.isHorizontal())
                submenu.show();
            else
                submenu.slideDown();
        }
    },
    
    deactivate: function(item) {
        var submenu =  item.children('div');
        item.removeClass('active-menuitem').find('.ink').remove();
        
        if(submenu.length) {
            submenu.hide();
            submenu.find('.ink').remove();
        }
    },

    deactivateItems: function(items, animate) {
        var $this = this;
        
        for(var i = 0; i < items.length; i++) {
            var item = items.eq(i),
            submenu = item.children('div');
            
            if (submenu.length) {
                if (item.hasClass('active-menuitem')) {
                    var activeSubItems = item.find('.active-menuitem');
                    item.removeClass('active-menuitem');
                    
                    if (animate) {
                        submenu.slideUp('normal', function() {
                            $(this).parent().find('.active-menuitem').each(function() {
                                $this.deactivate($(this));
                            });
                        });
                    }
                    else {
                        submenu.hide();
                        item.find('.active-menuitem').each(function() {
                            $this.deactivate($(this));
                        });
                    }
                    
                    $this.removeMenuitem(item.attr('id'));
                    activeSubItems.each(function() {
                        $this.removeMenuitem($(this).attr('id'));
                    });
                }
                else {
                    item.find('.active-menuitem').each(function() {
                        var subItem = $(this);
                        $this.deactivate(subItem);
                        $this.removeMenuitem(subItem.attr('id'));
                    });
                }
            }
            else if (item.hasClass('active-menuitem')) {
                $this.deactivate(item);
                $this.removeMenuitem(item.attr('id'));
            }
        }
    },
    
    removeMenuitem: function (id) {
        this.expandedMenuitems = $.grep(this.expandedMenuitems, function (value) {
            return value !== id;
        });
        this.saveMenuState();
    },
    
    addMenuitem: function (id) {
        if ($.inArray(id, this.expandedMenuitems) === -1) {
            this.expandedMenuitems.push(id);
        }
        this.saveMenuState();
    },
    
    saveMenuState: function() {
        if (!this.isHorizontal()) {            
            $.cookie('sapphire_expandeditems', this.expandedMenuitems.join(','), {path: '/'});
        }
    },
    
    clearMenuState: function() {
        $.removeCookie('sapphire_expandeditems', {path: '/'});
    },
    
    restoreMenuState: function() {
        var menuCookie = $.cookie('sapphire_expandeditems');
        if (menuCookie) {
            this.expandedMenuitems = menuCookie.split(',');
            for (var i = 0; i < this.expandedMenuitems.length; i++) {
                var id = this.expandedMenuitems[i];
                if (id) {
                    var menuitem = $("#" + this.expandedMenuitems[i].replace(/:/g, "\\:"));
                    menuitem.addClass('active-menuitem');
                    
                    var submenu = menuitem.children('div');
                    if(submenu.length) {
                        submenu.show();
                    }
                }
            }
        }
    },
        
    isMobile: function() {
        return window.innerWidth <= 1024;
    },

    isDesktop: function() {
        return window.innerWidth > 1024;
    },

    isHorizontal: function() {
        return this.wrapper.hasClass('layout-menu-horizontal') && this.isDesktop();
    },

    isVertical: function() {
        return this.wrapper.hasClass('layout-menu-vertical');
    }
});

/*!
 * jQuery Cookie Plugin v1.4.1
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2006, 2014 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {
	if (typeof define === 'function' && define.amd) {
		// AMD (Register as an anonymous module)
		define(['jquery'], factory);
	} else if (typeof exports === 'object') {
		// Node/CommonJS
		module.exports = factory(require('jquery'));
	} else {
		// Browser globals
		factory(jQuery);
	}
}(function ($) {

	var pluses = /\+/g;

	function encode(s) {
		return config.raw ? s : encodeURIComponent(s);
	}

	function decode(s) {
		return config.raw ? s : decodeURIComponent(s);
	}

	function stringifyCookieValue(value) {
		return encode(config.json ? JSON.stringify(value) : String(value));
	}

	function parseCookieValue(s) {
		if (s.indexOf('"') === 0) {
			// This is a quoted cookie as according to RFC2068, unescape...
			s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
		}

		try {
			// Replace server-side written pluses with spaces.
			// If we can't decode the cookie, ignore it, it's unusable.
			// If we can't parse the cookie, ignore it, it's unusable.
			s = decodeURIComponent(s.replace(pluses, ' '));
			return config.json ? JSON.parse(s) : s;
		} catch(e) {}
	}

	function read(s, converter) {
		var value = config.raw ? s : parseCookieValue(s);
		return $.isFunction(converter) ? converter(value) : value;
	}

	var config = $.cookie = function (key, value, options) {

		// Write

		if (arguments.length > 1 && !$.isFunction(value)) {
			options = $.extend({}, config.defaults, options);

			if (typeof options.expires === 'number') {
				var days = options.expires, t = options.expires = new Date();
				t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
			}

			return (document.cookie = [
				encode(key), '=', stringifyCookieValue(value),
				options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
				options.path    ? '; path=' + options.path : '',
				options.domain  ? '; domain=' + options.domain : '',
				options.secure  ? '; secure' : ''
			].join(''));
		}

		// Read

		var result = key ? undefined : {},
			// To prevent the for loop in the first place assign an empty array
			// in case there are no cookies at all. Also prevents odd result when
			// calling $.cookie().
			cookies = document.cookie ? document.cookie.split('; ') : [],
			i = 0,
			l = cookies.length;

		for (; i < l; i++) {
			var parts = cookies[i].split('='),
				name = decode(parts.shift()),
				cookie = parts.join('=');

			if (key === name) {
				// If second argument (value) is a function it's a converter...
				result = read(cookie, value);
				break;
			}

			// Prevent storing a cookie that we couldn't decode.
			if (!key && (cookie = read(cookie)) !== undefined) {
				result[name] = cookie;
			}
		}

		return result;
	};

	config.defaults = {};

	$.removeCookie = function (key, options) {
		// Must not alter options, thus extending a fresh object...
		$.cookie(key, '', $.extend({}, options, { expires: -1 }));
		return !$.cookie(key);
	};

}));

PrimeFaces.SapphireConfigurator = {

    changePrimaryColor: function(newColor) {
        var linkElement = $('link[href*="layout-"]');
        var href = linkElement.attr('href');
        var startIndexOf = href.indexOf('layout-') + 7;
        var endIndexOf = href.indexOf('.css');
        var currentColor = href.substring(startIndexOf, endIndexOf);
        this.replaceLink(linkElement, href.replace(currentColor, newColor));
    },

    changeSectionTheme: function(theme, section) {
        var wrapperElement = $('.layout-container');
        var styleClass = wrapperElement.attr('class');
        var tokens = styleClass.split(' ');
        var sectionClass;
        for (var i = 0; i < tokens.length; i++) {
            if (tokens[i].indexOf(section + '-') > -1) {
                sectionClass = tokens[i];
                break;
            }
        }

        wrapperElement.attr('class', styleClass.replace(sectionClass, section + '-' + theme));
    },

    changeMenuToHorizontal: function(value) {
        if (value) {
            $('.layout-container').addClass('layout-menu-horizontal');
        }
        else {
            $('.layout-container').removeClass('layout-menu-horizontal');
        }
    },

    beforeResourceChange: function() {
        PrimeFaces.ajax.RESOURCE = null;    //prevent resource append
    },

    changeComponentsTheme: function(theme, isCompactMode) {
        if (isCompactMode === 'true') {
            theme = theme + '-compact';
        }
        var library = 'primefaces-sapphire';
        var linkElement = $('link[href*="theme.css"]');
        var href = linkElement.attr('href');
        var index = href.indexOf(library) + 1;
        var currentTheme = href.substring(index + library.length);

        this.replaceLink(linkElement, href.replace(currentTheme, theme));
    },

    replaceLink: function(linkElement, href) {
        PrimeFaces.ajax.RESOURCE = 'javax.faces.Resource';
        
        var isIE = this.isIE();

        if (isIE) {
            linkElement.attr('href', href);
        }
        else {
            var cloneLinkElement = linkElement.clone(false);

            cloneLinkElement.attr('href', href);
            linkElement.after(cloneLinkElement);
            
            cloneLinkElement.off('load').on('load', function() {
                linkElement.remove();
            });
        }
    },

    isIE: function() {
        return /(MSIE|Trident\/|Edge\/)/i.test(navigator.userAgent);
    },

    updateInputStyle: function(value) {
        if (value === 'filled')
            $(document.body).addClass('ui-input-filled');
        else
            $(document.body).removeClass('ui-input-filled');
    }
}

if (PrimeFaces.widget.SelectOneMenu) {
    PrimeFaces.widget.SelectOneMenu = PrimeFaces.widget.SelectOneMenu.extend({
        init: function (cfg) {
            this._super(cfg);

            var $this = this;
            if (this.jq.parent().hasClass('ui-float-label')) {
                this.m_panel = $(this.jqId + '_panel');
                this.m_focusInput = $(this.jqId + '_focus');

                this.m_panel.addClass('ui-input-overlay-panel');
                this.jq.addClass('ui-inputwrapper');

                if (this.input.val() != '') {
                    this.jq.addClass('ui-inputwrapper-filled');
                }

                this.input.off('change').on('change', function () {
                    $this.inputValueControl($(this));
                });

                this.m_focusInput.on('focus.ui-selectonemenu', function () {
                    $this.jq.addClass('ui-inputwrapper-focus');
                })
                    .on('blur.ui-selectonemenu', function () {
                        $this.jq.removeClass('ui-inputwrapper-focus');
                    });

                if (this.cfg.editable) {
                    this.label.on('input', function (e) {
                        $this.inputValueControl($(this));
                    }).on('focus', function () {
                        $this.jq.addClass('ui-inputwrapper-focus');
                    }).on('blur', function () {
                        $this.jq.removeClass('ui-inputwrapper-focus');
                        $this.inputValueControl($(this));
                    });
                }
            }
        },

        inputValueControl: function (input) {
            if (input.val() != '')
                this.jq.addClass('ui-inputwrapper-filled');
            else
                this.jq.removeClass('ui-inputwrapper-filled');
        }
    });
}

if (PrimeFaces.widget.Chips) {
    PrimeFaces.widget.Chips = PrimeFaces.widget.Chips.extend({
        init: function (cfg) {
            this._super(cfg);

            var $this = this;
            if (this.jq.parent().hasClass('ui-float-label')) {
                this.jq.addClass('ui-inputwrapper');

                if ($this.jq.find('.ui-chips-token').length !== 0) {
                    this.jq.addClass('ui-inputwrapper-filled');
                }

                this.input.on('focus.ui-chips', function () {
                    $this.jq.addClass('ui-inputwrapper-focus');
                }).on('input.ui-chips', function () {
                    $this.inputValueControl();
                }).on('blur.ui-chips', function () {
                    $this.jq.removeClass('ui-inputwrapper-focus');
                    $this.inputValueControl();
                });

            }
        },

        inputValueControl: function () {
            if (this.jq.find('.ui-chips-token').length !== 0 || this.input.val() != '')
                this.jq.addClass('ui-inputwrapper-filled');
            else
                this.jq.removeClass('ui-inputwrapper-filled');
        }
    });
}

if (PrimeFaces.widget.DatePicker) {
    PrimeFaces.widget.DatePicker = PrimeFaces.widget.DatePicker.extend({
        init: function (cfg) {
            this._super(cfg);

            var $this = this;
            if (this.jq.parent().hasClass('ui-float-label') && !this.cfg.inline) {
                if (this.input.val() != '') {
                    this.jq.addClass('ui-inputwrapper-filled');
                }

                this.jqEl.off('focus.ui-datepicker blur.ui-datepicker change.ui-datepicker')
                    .on('focus.ui-datepicker', function () {
                        $this.jq.addClass('ui-inputwrapper-focus');
                    })
                    .on('blur.ui-datepicker', function () {
                        $this.jq.removeClass('ui-inputwrapper-focus');
                    })
                    .on('change.ui-datepicker', function () {
                        $this.inputValueControl($(this));
                    });
            }
        },

        inputValueControl: function (input) {
            if (input.val() != '')
                this.jq.addClass('ui-inputwrapper-filled');
            else
                this.jq.removeClass('ui-inputwrapper-filled');
        }
    });
}