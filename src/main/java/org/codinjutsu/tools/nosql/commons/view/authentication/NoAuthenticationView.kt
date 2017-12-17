package org.codinjutsu.tools.nosql.commons.view.authentication

import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings
import javax.swing.JPanel

internal class NoAuthenticationView : AuthenticationView {

    override val component: JPanel
        get() = JPanel()

    override fun create() = AuthenticationSettings()

    override fun load(settings: AuthenticationSettings) {
    }
}