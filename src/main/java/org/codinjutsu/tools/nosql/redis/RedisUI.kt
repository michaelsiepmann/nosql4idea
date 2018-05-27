/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql.redis

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.DatabaseUI
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.redis.logic.RedisClient
import org.codinjutsu.tools.nosql.redis.model.RedisContext
import org.codinjutsu.tools.nosql.redis.view.RedisPanel
import org.codinjutsu.tools.nosql.redis.view.authentication.RedisAuthenticationPanel
import org.codinjutsu.tools.nosql.redis.view.editor.RedisObjectFile

class RedisUI : DatabaseUI {
    override fun createAuthenticationView() = RedisAuthenticationPanel()

    override fun createResultPanel(project: Project, objectFile: NoSqlDatabaseObjectFile): DatabasePanel {
        val redisObjectFile = objectFile as RedisObjectFile
        return RedisPanel(project, RedisContext(RedisClient.getInstance(project), redisObjectFile.configuration, redisObjectFile.database))
    }
}
